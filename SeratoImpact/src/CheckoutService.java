import java.math.BigDecimal;
import java.sql.Array;
import java.util.*;
public final class CheckoutService {
    private CheckoutService() {}
    public static final class ResolvedLine {
        public final Binder binder;
        public final InventoryItem item;
        public final int quantity;

        ResolvedLine(Binder binder, InventoryItem item, int quantity) {
            this.binder = binder;
            this.item = item;
            this.quantity = quantity;
        }

        public BigDecimal subtotal() {
            return item.getListing().getPrice().multiply(BigDecimal.valueOf(quantity));
        }

        public static ResolvedLine resolve(Marketplace marketplace, CartItem cartItem) {
            for (Binder binder : marketplace.getBinders()) {
                if (binder.getBinderId().equals(cartItem.getBinderId())) {
                    for (InventoryItem item : binder.getItems()) {
                        if (item.getItemId().equals(cartItem.getInventoryItemId())) {
                            return new ResolvedLine(binder, item, cartItem.getQuantity());
                        }
                    }
                }
            }
            throw new IllegalArgumentException("A card in your cart is no longer listed. Remove it and try again.");
        }

        public static BigDecimal currentTotal(Marketplace marketplace, Cart cart) {
            BigDecimal total = BigDecimal.ZERO;

            for (CartItem entry : cart.getItems()) {
                total = total.add(resolve(marketplace, entry).subtotal());
            }

            return total;
        }

        public static synchronized List<Order> checkout(Marketplace marketplace, Cart cart, String buyerName, String buyerContact) {
            if (buyerName == null || buyerName.isBlank() || buyerContact == null || buyerContact.isBlank()) {
                throw new IllegalArgumentException("Enter buyer name and a contact number or email.");
            }

            if (cart.isEmpty()) {
                throw new IllegalArgumentException("Your cart is empty");
            }

            Map<String, List<ResolvedLine>> bySeller = new LinkedHashMap<>();

            for (CartItem entry : cart.getItems()) {
                ResolvedLine line = resolve(marketplace, entry);

                if (!line.binder.isPublic() || !line.item.isAvailableForSale()) {
                    throw new IllegalArgumentException(line.item.getCard().getName() + " is no longer for sale.");
                }

                if (line.quantity <= 0 || line.quantity > line.item.getQuantity()) {
                    throw new IllegalArgumentException("Insufficient stock for " + line.item.getCard().getName() + ". Available: " + line.item.getQuantity());
                }

                bySeller.computeIfAbsent(line.binder.getOwnerId(), x->new ArrayList<>()).add(line);
            }

            List<Order> newOrders = new ArrayList<>();
            for (Map.Entry<String, List<ResolvedLine>> seller : bySeller.entrySet()) {
                List<OrderItem> snapshot = new ArrayList<>();

                for (ResolvedLine line : seller.getValue()) {
                    snapshot.add(new OrderItem(
                            line.binder.getBinderId(),
                            line.item.getItemId(),
                            line.item.getCard().getName(),
                            line.item.getCondition(),
                            line.quantity,
                            line.item.getListing().getPrice()
                    ));
                }

                newOrders.add(new Order(  //changed
                        seller.getKey(),
                        null,
                        buyerName.trim(),
                        buyerContact.trim(),
                        snapshot
                ));
            }

            for (List<ResolvedLine> lines : bySeller.values()) {
                for (ResolvedLine line : lines) {
                    line.item.sell(line.quantity);
                }
            }

            for (Order order : newOrders) {
                marketplace.addOrder(order);
            }
            cart.clear();
            return newOrders;
        }

        public static void cancelAndRestock(Marketplace marketplace, Order order) {
            if (order.getStatus() != OrderStatus.AWAITING_PICKUP) {
                throw new IllegalArgumentException("Order is not awaiting pickup.");
            }
            List<ResolvedLine> toRestore = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                CartItem lookup = new CartItem(item.getBinderId(), item.getInventoryItemId(), item.getQuantity());
                ResolvedLine line = resolve(marketplace, lookup);
                if (!line.binder.getOwnerId().equals(order.getSellerId())) {
                    throw new IllegalArgumentException("Order seller does not match.");
                }
                toRestore.add(line);
            }
            for (ResolvedLine line : toRestore) {
                line.item.restock(line.quantity);
            }
            order.cancel();
        }
    }
}
