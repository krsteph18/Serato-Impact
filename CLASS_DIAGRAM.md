# Revised UML: Binder Marketplace Only

```mermaid
classDiagram
    direction TB

    class Card {
        -String cardId
        -String name
        -String category
        -String setName
        -String rarity
        -String imagePath
        +update(...) void
    }
    class InventoryItem {
        -String itemId
        -Card card
        -String condition
        -int quantity
        -Listing listing
        +update(...) void
        +isAvailableForSale() boolean
    }
    class Listing {
        -BigDecimal price
        -boolean forSale
        +update(price, forSale) void
    }
    class Binder {
        -String binderId
        -String ownerId
        -String name
        -String description
        -boolean isPublic
        -List~InventoryItem~ items
        +addCard(item) void
        +removeCard(item) void
        +getVisibleItems(sellerMode) List~InventoryItem~
        +getTotalPages(sellerMode) int
    }
    class User {
        -String userId
        -String displayName
        -String contact
        +update(name, contact) void
    }
    class Searchable {
        <<interface>>
        +searchCards(keyword) List~InventoryItem~
    }
    class Marketplace {
        -List~User~ sellers
        -List~Binder~ binders
        +addSeller(name, contact) User
        +addBinder(seller, name, description, isPublic) Binder
        +publicBinders() List~Binder~
        +bindersFor(seller) List~Binder~
        +searchCards(keyword) List~InventoryItem~
    }
    class MarketplaceRepository {
        +load() Marketplace
        +save(marketplace) void
    }
    class Main {
        -Marketplace marketplace
        -CardLayout navigation
        +main(args) void
    }
    class SeratoImpactLook {
        +show(seller, sellerMode) void
        +refresh() void
    }
    class BinderCoverForm {
        +setBinder(binder, owner, sellerMode) void
    }
    class BinderPagesForm {
        +setBinder(binder, sellerMode) void
        +refresh() void
    }
    class BinderEditorForm {
        +show(parent, existing) BinderEditorForm
    }
    class CardEditorForm {
        +show(parent, existing) CardEditorForm
    }
    class SellerEditorForm {
        +show(parent, existing) SellerEditorForm
    }

    Searchable <|.. Marketplace
    Marketplace "1" o-- "0..*" User : stores
    Marketplace "1" o-- "0..*" Binder : indexes
    User "1" --> "0..*" Binder : owns
    Binder "1" *-- "0..*" InventoryItem : contains
    InventoryItem "1" *-- "1" Card : describes
    InventoryItem "1" *-- "1" Listing : pricing and sale status
    MarketplaceRepository ..> Marketplace : persists
    Main ..> Marketplace : manages
    Main ..> SeratoImpactLook : navigates
    Main ..> BinderCoverForm : navigates
    Main ..> BinderPagesForm : navigates
    Main ..> BinderEditorForm : edits
    Main ..> CardEditorForm : edits
    Main ..> SellerEditorForm : edits
```

## Important notes

- Card is now a **concrete data class**. No combat or action-card inheritance is required to catalog cards.
- Card type/group is descriptive metadata (`category`); there is no `play()`, `takeDamage()`, dice, skill or summon method.
- InventoryItem owns a card description and a listing, and tracks stock and condition. A listing that is not marked for sale remains visible to the owner but hidden from buyers.
- Buyer and seller are **view modes** of the same local prototype. No buyer authentication, reservation, transaction, or checkout subsystem exists.
- Page number is derived from the ordered inventory, nine cards at a time. It is not stored as a separate entity.
