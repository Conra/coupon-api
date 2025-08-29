package com.conrado.couponapi.service.solver;

import com.conrado.couponapi.model.CouponResult;
import com.conrado.couponapi.model.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CouponSolverBitSetImpl implements CouponSolver {

    // Limit to keep BitSet indices in int range and memory reasonable (tweak as needed)
    private static final int MAX_AMOUNT_CENTS = 200_000_000; // $2,000,000.00

    /** Main entry: returns item IDs that maximize total <= amount, plus the total. */
    @Override
    public CouponResult solve(List<Item> items, BigDecimal amount) {
        // 1) Normalize to cents (integers)
        int amountCents = toCents(amount);
        if (amountCents < 0 || amountCents > MAX_AMOUNT_CENTS) {
            throw new IllegalArgumentException("Amount too large for BitSet DP.");
        }

        // Filter invalid/too expensive items and convert prices to cents
        List<ItemCents> clean = items.stream()
                .filter(it -> it != null && it.getId() != null && it.getPrice() != null)
                .map(it -> new ItemCents(it.getId(), toCents(it.getPrice())))
                .filter(ic -> ic.price > 0 && ic.price <= amountCents)
                .toList();

        int n = clean.size();
        if (n == 0) return new CouponResult(Collections.emptyList(), BigDecimal.ZERO);

        // 2) create bitset
        BitSet bs = new BitSet(amountCents + 1);
        bs.set(0);

        // parent[s] = index of item used to first reach s; prev[s] = previous sum (s - p)
        int[] parent = new int[amountCents + 1];
        int[] prev = new int[amountCents + 1];
        Arrays.fill(parent, -1);

        for (int i = 0; i < n; i++) {
            int p = clean.get(i).price;
            // shifted = dp << p  (only up to amountCents)
            BitSet shifted = shiftLeftBounded(bs, p, amountCents);

            // newOnes = shifted & ~dp  (states turned on for the first time by taking item i)
            BitSet newOnes = (BitSet) shifted.clone();
            newOnes.andNot(bs);

            for (int s = newOnes.nextSetBit(0); s >= 0; s = newOnes.nextSetBit(s + 1)) {
                parent[s] = i;
                prev[s] = s - p;
            }
            bs.or(shifted);

            // Optional early stop if we hit the exact amount
            if (bs.get(amountCents)) break;
        }

        // 3) Best sum ≤ amount
        int best = bs.previousSetBit(amountCents);
        if (best < 0) return new CouponResult(Collections.emptyList(), BigDecimal.ZERO);

        // 4) Reconstruct chosen items
        List<String> chosen = new ArrayList<>();
        for (int s = best; s > 0; s = prev[s]) {
            int i = parent[s];
            if (i < 0) break; // safety
            chosen.add(clean.get(i).id);
        }
        Collections.reverse(chosen);

        return new CouponResult(chosen, fromCents(best));
    }

    /** BitSet left shift truncated to [0 limit]. */
    private static BitSet shiftLeftBounded(BitSet src, int shift, int limit) {
        BitSet out = new BitSet(limit + 1);
        for (int s = src.nextSetBit(0); s >= 0; s = src.nextSetBit(s + 1)) {
            int ns = s + shift;
            if (ns <= limit) out.set(ns);
            else break; // because nextSetBit is increasing; once we exceed, we can stop
        }
        return out;
    }

    private static int toCents(BigDecimal amount) {
        return amount
                .movePointRight(2)   // two decimals -> cents
                .setScale(0)         // exact integer cents
                .intValueExact();    // throws if out of int range
    }

    private static BigDecimal fromCents(int cents) {
        return new BigDecimal(cents).movePointLeft(2);
    }

    private record ItemCents(String id, int price) {}
}
