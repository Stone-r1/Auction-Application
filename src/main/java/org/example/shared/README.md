# Note on `/shared` directory

The `/shared` module contains framework-agnostic contracts used across multiple bounded contexts.
It includes shared DTOs, exceptions, and integration interfaces. These types must not depend on any infrastructure or framework-specific annotations.

Shared DTOs act as integration contracts between modules and should not leak internal domain models. 
Repository interfaces in this module are limited to cross-cutting read or integration needs and must not expose persistence details.