package com.ubs.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http
    .ResponseEntity;
import org.springframework.web.bind
    .annotation.GetMapping;
import org.springframework.web.bind
    .annotation.PathVariable;
import org.springframework.web.bind
    .annotation.PostMapping;
import org.springframework.web.bind
    .annotation.RequestBody;
import org.springframework.web.bind
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RequestParam;
import org.springframework.web.bind
    .annotation.RestController;

import com.ubs.ledger.exception
    .ResourceNotFoundException;
import com.ubs.ledger.model.Settlement;
import com.ubs.ledger.model.Trade;
import com.ubs.ledger.model.TradeAudit;
import com.ubs.ledger.service.TradeService;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            TradeController.class
        );

    private static final SimpleDateFormat
        DATE_FMT = new SimpleDateFormat(
            "yyyy-MM-dd"
        );

    private final TradeService svc;

    public TradeController(
        TradeService svc
    ) {
        this.svc = svc;
    }

    @GetMapping
    public Map<String, Object> list(
        @RequestParam(
            defaultValue = "200"
        ) int limit
    ) {
        List<Trade> trades =
            svc.listTrades(limit);
        return Map.of(
            "trades", trades,
            "count", trades.size()
        );
    }

    @GetMapping("/search")
    public Map<String, Object> search(
        @RequestParam
        Map<String, String> params
    ) {
        params.remove("limit");
        List<Trade> trades =
            svc.searchTrades(params);
        return Map.of(
            "trades", trades,
            "count", trades.size()
        );
    }

    @GetMapping("/{tradeId}")
    public Map<String, Object> get(
        @PathVariable long tradeId
    ) {
        Trade trade =
            svc.getTradeDetail(tradeId);
        if (trade == null) {
            throw
                new ResourceNotFoundException(
                    "trade not found"
                );
        }
        List<Settlement> settlements =
            svc.getSettlements(tradeId);
        List<TradeAudit> audit =
            svc.getAuditTrail(tradeId);

        return Map.of(
            "trade", trade,
            "settlements", settlements,
            "audit_trail", audit
        );
    }

    @PostMapping
    public ResponseEntity<
        Map<String, Object>
    > book(
        @RequestBody
        Map<String, Object> body
    ) throws Exception {
        var required = List.of(
            "trade_ref", "trade_date",
            "settle_date", "trader_id",
            "cp_id", "instr_id",
            "direction", "quantity",
            "price", "currency"
        );
        var missing =
            new ArrayList<String>();
        for (var f : required) {
            if (!body.containsKey(f)) {
                missing.add(f);
            }
        }
        if (!missing.isEmpty()) {
            throw
                new IllegalArgumentException(
                    "missing fields: "
                    + missing
                );
        }

        Date tradeDate;
        Date settleDate;
        synchronized (DATE_FMT) {
            tradeDate = DATE_FMT.parse(
                (String) body.get(
                    "trade_date"
                )
            );
            settleDate = DATE_FMT.parse(
                (String) body.get(
                    "settle_date"
                )
            );
        }

        double accruedInt = 0.0;
        if (body.containsKey(
            "accrued_int"
        )) {
            accruedInt =
                ((Number) body.get(
                    "accrued_int"
                )).doubleValue();
        }

        long newId = svc.bookTrade(
            (String) body.get("trade_ref"),
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
            (String) body.get("direction"),
            ((Number) body.get(
                "quantity"
            )).doubleValue(),
            ((Number) body.get(
                "price"
            )).doubleValue(),
            (String) body.get("currency"),
            accruedInt
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Map.of(
                "message", "trade booked",
                "new_trade_id", newId
            ));
    }

    @PostMapping("/{tradeId}/match")
    public Map<String, Object> match(
        @PathVariable long tradeId,
        @RequestBody(required = false)
        Map<String, Object> body
    ) {
        String matchedBy = "SYSTEM";
        if (body != null
            && body.containsKey(
                "matched_by"
            )) {
            matchedBy = (String)
                body.get("matched_by");
        }
        svc.matchTrade(
            tradeId, matchedBy
        );
        return Map.of(
            "message", "trade matched",
            "trade_id", tradeId
        );
    }

    @PostMapping("/{tradeId}/settle")
    public Map<String, Object> settle(
        @PathVariable long tradeId,
        @RequestBody(required = false)
        Map<String, Object> body
    ) {
        String method = "DVP";
        if (body != null
            && body.containsKey(
                "settle_method"
            )) {
            method = (String)
                body.get("settle_method");
        }
        svc.settleTrade(tradeId, method);
        return Map.of(
            "message",
            "settlement initiated",
            "trade_id", tradeId
        );
    }

    @PostMapping("/{tradeId}/retry")
    public Map<String, Object> retry(
        @PathVariable long tradeId,
        @RequestBody(required = false)
        Map<String, Object> body
    ) {
        String method = null;
        if (body != null
            && body.containsKey(
                "settle_method"
            )) {
            method = (String)
                body.get("settle_method");
        }
        svc.retryFailed(tradeId, method);
        return Map.of(
            "message", "trade re-queued",
            "trade_id", tradeId
        );
    }

    @GetMapping("/{tradeId}/audit")
    public Map<String, Object> audit(
        @PathVariable long tradeId
    ) {
        List<TradeAudit> trail =
            svc.getAuditTrail(tradeId);
        return Map.of(
            "trade_id", tradeId,
            "audit_trail", trail,
            "count", trail.size()
        );
    }
}
