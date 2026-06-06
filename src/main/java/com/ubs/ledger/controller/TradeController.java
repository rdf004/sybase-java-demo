package com.ubs.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.http
    .HttpStatus;
import org.springframework.http
    .ResponseEntity;
import org.springframework.stereotype
    .Controller;
import org.springframework.web.bind
    .annotation.PathVariable;
import org.springframework.web.bind
    .annotation.RequestBody;
import org.springframework.web.bind
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RequestMethod;
import org.springframework.web.bind
    .annotation.RequestParam;
import org.springframework.web.bind
    .annotation.ResponseBody;

import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Settlement;
import com.ubs.ledger.model.Trade;
import com.ubs.ledger.model.TradeAudit;
import com.ubs.ledger.service.TradeService;

/**
 * REST controller for trade operations.
 * Handles CRUD, lifecycle, and audit
 * trail endpoints.
 *
 * NOTE: We use @Controller + @ResponseBody
 * instead of @RestController because
 * @RestController was added in Spring 4.0.
 *
 * @author Platform Engineering
 * @since 1.0
 */
@Controller
@RequestMapping("/trades")
public class TradeController {

    private static final Logger LOG =
        Logger.getLogger(
            TradeController.class
        );

    private static final SimpleDateFormat
        DATE_FMT = new SimpleDateFormat(
            "yyyy-MM-dd"
        );

    @Autowired
    private TradeService tradeService;

    /**
     * GET /api/trades
     * List trades with optional filters.
     */
    @RequestMapping(
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > listTrades(
        @RequestParam(
            value = "status",
            required = false
        ) String status,
        @RequestParam(
            value = "trader_id",
            required = false
        ) String traderId,
        @RequestParam(
            value = "cp_id",
            required = false
        ) String cpId,
        @RequestParam(
            value = "direction",
            required = false
        ) String direction,
        @RequestParam(
            value = "currency",
            required = false
        ) String currency
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            Map<String, String> filters =
                new HashMap<String, String>();
            if (status != null) {
                filters.put(
                    "status", status
                );
            }
            if (traderId != null) {
                filters.put(
                    "trader_id", traderId
                );
            }
            if (cpId != null) {
                filters.put(
                    "cp_id", cpId
                );
            }
            if (direction != null) {
                filters.put(
                    "direction", direction
                );
            }
            if (currency != null) {
                filters.put(
                    "currency", currency
                );
            }

            List<Trade> trades;
            if (filters.isEmpty()) {
                trades =
                    tradeService.listTrades(
                        200
                    );
            } else {
                trades =
                    tradeService.searchTrades(
                        filters
                    );
            }

            response.put(
                "trades", trades
            );
            response.put(
                "count", trades.size()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "List trades failed: "
                + e.getMessage()
            );
            response.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * GET /api/trades/{tradeId}
     * Get trade detail with settlements
     * and audit trail.
     */
    @RequestMapping(
        value = "/{tradeId}",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > getTrade(
        @PathVariable long tradeId
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            Trade trade =
                tradeService.getTradeDetail(
                    tradeId
                );
            if (trade == null) {
                response.put(
                    "error",
                    "trade not found"
                );
                return new ResponseEntity
                    <Map<String, Object>>(
                        response,
                        HttpStatus.NOT_FOUND
                    );
            }

            response.put("trade", trade);

            List<Settlement> settlements =
                tradeService.getSettlements(
                    tradeId
                );
            response.put(
                "settlements",
                settlements
            );

            List<TradeAudit> audit =
                tradeService.getAuditTrail(
                    tradeId
                );
            response.put(
                "audit_trail", audit
            );

            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "Get trade failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            response.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * POST /api/trades
     * Book a new trade via sp_book_trade.
     */
    @RequestMapping(
        method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > bookTrade(
        @RequestBody
        Map<String, Object> body
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            // validate required fields
            String[] required = {
                "trade_ref", "trade_date",
                "settle_date", "trader_id",
                "cp_id", "instr_id",
                "direction", "quantity",
                "price", "currency"
            };
            List<String> missing =
                new ArrayList<String>();
            for (String field : required) {
                if (!body.containsKey(
                    field
                )) {
                    missing.add(field);
                }
            }
            if (!missing.isEmpty()) {
                response.put(
                    "error",
                    "missing fields: "
                    + missing.toString()
                );
                return new ResponseEntity
                    <Map<String, Object>>(
                        response,
                        HttpStatus
                        .BAD_REQUEST
                    );
            }

            Date tradeDate =
                DATE_FMT.parse(
                    (String) body.get(
                        "trade_date"
                    )
                );
            Date settleDate =
                DATE_FMT.parse(
                    (String) body.get(
                        "settle_date"
                    )
                );

            double accruedInt = 0.0;
            if (body.containsKey(
                "accrued_int"
            )) {
                accruedInt =
                    ((Number) body.get(
                        "accrued_int"
                    )).doubleValue();
            }

            long newId =
                tradeService.bookTrade(
                    (String) body.get(
                        "trade_ref"
                    ),
                    tradeDate,
                    settleDate,
                    ((Number) body.get(
                        "trader_id"
                    )).longValue(),
                    ((Number) body.get(
                        "cp_id"
                    )).longValue(),
                    ((Number) body.get(
                        "instr_id"
                    )).longValue(),
                    (String) body.get(
                        "direction"
                    ),
                    ((Number) body.get(
                        "quantity"
                    )).doubleValue(),
                    ((Number) body.get(
                        "price"
                    )).doubleValue(),
                    (String) body.get(
                        "currency"
                    ),
                    accruedInt
                );

            response.put(
                "message", "trade booked"
            );
            response.put(
                "new_trade_id", newId
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus.CREATED
                );
        } catch (LedgerException e) {
            LOG.error(
                "Book trade failed: "
                + e.getMessage()
            );
            response.put(
                "error", "booking failed"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        } catch (Exception e) {
            LOG.error(
                "Book trade parse error: "
                + e.getMessage()
            );
            response.put(
                "error",
                "invalid request data"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .BAD_REQUEST
                );
        }
    }

    /**
     * POST /api/trades/{tradeId}/match
     */
    @RequestMapping(
        value = "/{tradeId}/match",
        method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > matchTrade(
        @PathVariable long tradeId,
        @RequestBody(required = false)
        Map<String, Object> body
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            String matchedBy = "SYSTEM";
            if (body != null
                && body.containsKey(
                    "matched_by"
                )) {
                matchedBy = (String)
                    body.get("matched_by");
            }

            tradeService.matchTrade(
                tradeId, matchedBy
            );

            response.put(
                "message", "trade matched"
            );
            response.put(
                "trade_id", tradeId
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "Match failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            response.put(
                "error", e.getMessage()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * POST /api/trades/{tradeId}/settle
     */
    @RequestMapping(
        value = "/{tradeId}/settle",
        method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > settleTrade(
        @PathVariable long tradeId,
        @RequestBody(required = false)
        Map<String, Object> body
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            String method = "DVP";
            if (body != null
                && body.containsKey(
                    "settle_method"
                )) {
                method = (String)
                    body.get(
                        "settle_method"
                    );
            }

            tradeService.settleTrade(
                tradeId, method
            );

            response.put(
                "message",
                "settlement initiated"
            );
            response.put(
                "trade_id", tradeId
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "Settle failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            response.put(
                "error", e.getMessage()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * POST /api/trades/{tradeId}/retry
     */
    @RequestMapping(
        value = "/{tradeId}/retry",
        method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > retryTrade(
        @PathVariable long tradeId,
        @RequestBody(required = false)
        Map<String, Object> body
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            String method = null;
            if (body != null
                && body.containsKey(
                    "settle_method"
                )) {
                method = (String)
                    body.get(
                        "settle_method"
                    );
            }

            tradeService.retryFailed(
                tradeId, method
            );

            response.put(
                "message",
                "trade re-queued"
            );
            response.put(
                "trade_id", tradeId
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "Retry failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            response.put(
                "error", e.getMessage()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * GET /api/trades/{tradeId}/audit
     */
    @RequestMapping(
        value = "/{tradeId}/audit",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > tradeAudit(
        @PathVariable long tradeId
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            List<TradeAudit> audit =
                tradeService.getAuditTrail(
                    tradeId
                );
            response.put(
                "trade_id", tradeId
            );
            response.put(
                "audit_trail", audit
            );
            response.put(
                "count", audit.size()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "Audit failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            response.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }
}
