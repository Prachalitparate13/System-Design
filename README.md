# Creational Design Patterns in Java: Builder, Factory, Singleton, Abstract Factory, Prototype

A concise reference for five creational design patterns: what each one is, what problem it solves, and the essential parts needed to implement it.

## Contents

- [1. Builder Pattern](#1-builder-pattern)
- [2. Factory Pattern](#2-factory-pattern)
- [3. Singleton Pattern](#3-singleton-pattern)
- [4. Abstract Factory Pattern](#4-abstract-factory-pattern)
- [5. Prototype Pattern](#5-prototype-pattern)
- [Quick Comparison](#quick-comparison)

---

## 1. Builder Pattern

### Definition

Builder separates the construction of a complex object from its final representation. The object is assembled step by step through a fluent API, and the finished object is produced by a single `build()` call.

### Problem it solves

- Constructors with many parameters are hard to read, and it is easy to pass arguments in the wrong order (for example, two `String` values swapped).
- Classes with many optional fields lead to a "telescoping constructor" problem: a pile of overloaded constructors for every combination.
- Setters make objects mutable and allow them to exist in a half-built, invalid state.

### Necessary parts to implement

1. **Product class with a private constructor.** The constructor takes the builder as its only argument, so the object can only be created through the builder.
2. **Final fields in the product.** This makes the built object immutable.
3. **Static nested `Builder` class.** It holds a copy of every field of the product.
4. **Required fields passed into the Builder's constructor.** This guarantees mandatory values cannot be forgotten.
5. **Optional fields with sensible defaults in the Builder.** Callers set only what they need.
6. **Fluent setter methods that return `this`.** This enables method chaining.
7. **A `build()` method.** It validates the data in one place and then calls the product's private constructor.
8. **A static `builder()` entry method on the product (optional but common).** It gives callers a clean starting point.

### Use when

- A class has many parameters, especially several optional ones.
- You want immutable objects.
- Validation should happen before the object exists.

### Pros and cons

| Pros | Cons |
|---|---|
| Readable, named construction | More boilerplate (Lombok's `@Builder` removes most of it) |
| Immutable result | Extra builder object created per instance |
| Required vs. optional fields are explicit | Overkill for classes with 2-3 fields |
| Validation centralized in `build()` | |

---

## 2. Factory Pattern

### Definition

Factory provides a dedicated place to create objects and hides which concrete class is instantiated. Callers ask the factory for an object by type or configuration and receive it through a common interface.

### Problem it solves

- Code littered with `new ConcreteClass()` is tightly coupled to specific implementations.
- Adding a new type forces changes at every place that creates objects.
- Creation logic that is more than a trivial constructor call gets duplicated across the codebase.

### Necessary parts to implement

1. **Product interface (or abstract class).** It defines the common contract that every product fulfils.
2. **Concrete product classes.** Each one implements the interface.
3. **Factory class with a creation method.** The method returns the interface type, never the concrete type.
4. **A selector.** This is usually an enum or a parameter that tells the factory which product to build. An enum is preferred over a raw `String` for compile-time safety.
5. **Private constructor on the factory (static factory).** The factory is a utility and is never instantiated.

**Factory Method variant (GoF):**

1. An abstract creator class that declares an abstract factory method.
2. Subclasses that override the factory method to return their own product.
3. Business logic in the creator that uses the product only through the interface.

**Spring Boot variant:** annotate each implementation as a bean, inject all of them into the factory as a map keyed by bean name, and look them up by key. New types then need only a new bean class, with no change to the factory.

### Use when

- The exact type to create depends on input, configuration or environment.
- You want to add new types without touching calling code.
- Creation logic should live in one place.

### Pros and cons

| Pros | Cons |
|---|---|
| Callers depend on interfaces, not concrete classes | More classes to maintain |
| New types need minimal change | A static factory's `switch` must be edited for each new type |
| Creation logic is centralized | Overkill when there is only one implementation |

---

## 3. Singleton Pattern

### Definition

Singleton ensures that a class has exactly one instance and provides a single, global point of access to it.

### Problem it solves

- Some resources must exist only once, such as application configuration, a logger, a connection pool or a registry.
- Multiple instances of such resources would waste memory, cause inconsistent state or create conflicting access to a shared resource.

### Necessary parts to implement

1. **Private constructor.** This prevents any other class from calling `new` on it.
2. **Private static field that holds the single instance.** Often `final` for eager initialization, or `volatile` for lazy double-checked locking.
3. **Public static accessor method** (such as `getInstance()`). This is the only way for callers to obtain the instance.
4. **Thread safety.** Choose one approach:
   - Eager initialization (instance created at class load)
   - Double-checked locking with a `volatile` field
   - Initialization-on-demand holder (a private static inner class holds the instance)
   - Enum with a single `INSTANCE` constant
5. **Protection against reflection and serialization (if required).** Reflection can call a private constructor, and deserialization can create a second instance. The enum approach is immune to both.
6. **Stateless design or thread-safe state.** A singleton with mutable state is effectively a global variable, so any shared state must be thread-safe.

### Implementation options at a glance

| Approach | Lazy | Thread-safe | Reflection/serialization safe |
|---|---|---|---|
| Enum | Yes | Yes | Yes |
| Double-checked locking | Yes | Yes (with `volatile`) | No |
| Holder idiom | Yes | Yes | No |
| Eager | No | Yes | No |

### Spring Boot note

Beans are singleton-scoped by default, so you rarely hand-write the pattern. Spring's version is easier to test because instances can be injected and mocked. Note that "singleton" in Spring means one instance per application context, not one per JVM.

### Use when

- Exactly one shared instance is needed across the application.

### Pros and cons

| Pros | Cons |
|---|---|
| Controlled access to a single instance | Hard to mock in unit tests (static access) |
| Saves resources | Acts like global state if it holds mutable data |
| Lazy initialization possible | Can hide dependencies between classes |

---

## 4. Abstract Factory Pattern

### Definition

Abstract Factory provides an interface for creating **families of related objects** without specifying their concrete classes. Where a Factory creates one kind of product, an Abstract Factory creates a whole set of products that are designed to work together.

### Problem it solves

- An application needs several related objects (for example a button, a checkbox and a text field) that must all belong to the same family, such as a Windows theme or a Dark theme.
- Mixing products from different families (a Windows button with a Mac checkbox) causes inconsistent or broken behaviour.
- Client code should not need to know which family it is using, and switching families should not require changing that code.

### Necessary parts to implement

1. **Abstract product interfaces.** One interface per kind of product in the family (for example `Button`, `Checkbox`).
2. **Concrete products.** One implementation of each product interface per family (for example `WindowsButton`, `MacButton`).
3. **Abstract factory interface.** It declares one creation method per product type, each returning the abstract product interface.
4. **Concrete factories.** One per family. Each implements the abstract factory and returns only products from its own family, which guarantees consistency.
5. **Client code that depends only on the interfaces.** It receives an abstract factory (usually via constructor injection) and never references a concrete class.
6. **A place to choose the concrete factory.** This is typically done once at startup based on configuration, environment or user settings.

### Use when

- Products naturally come in families that must be used together.
- You need to switch an entire family (theme, database vendor, cloud provider, OS) without touching client code.
- You want to enforce that products from different families are never mixed.

### Pros and cons

| Pros | Cons |
|---|---|
| Guarantees products from one family are compatible | Many interfaces and classes |
| Client is decoupled from concrete classes | Adding a new product type means changing the abstract factory and every concrete factory |
| Swapping a whole family is a one-line change | Overkill if there is only one family |

### Factory vs. Abstract Factory

| | Factory | Abstract Factory |
|---|---|---|
| Creates | One product type | A family of related product types |
| Structure | One factory method or class | A factory interface with several creation methods |
| Focus | Which class to instantiate | Which family of classes to use together |

---

## 5. Prototype Pattern

### Definition

Prototype creates new objects by **copying (cloning) an existing instance**, called the prototype, instead of constructing them from scratch.

### Problem it solves

- Creating an object is expensive (heavy computation, database or network calls, complex setup) and you need many similar copies.
- You want a new object that starts with the same state as an existing one, then modify only a few fields.
- Client code should be able to copy an object without knowing its concrete class, for example when it only holds a reference to an interface.

### Necessary parts to implement

1. **Prototype interface or abstract class.** It declares a copy method (such as `copy()` or `clone()`).
2. **Concrete prototype classes.** Each one implements the copy method and returns a new instance of itself with the same field values.
3. **A decision on shallow vs. deep copy.**
   - Shallow copy duplicates the object but shares references to nested objects.
   - Deep copy also duplicates nested mutable objects, so the copy is fully independent. This is required whenever the object holds mutable collections or other objects.
4. **A copy constructor (recommended) or `Cloneable` with `clone()` override.** Java's `Cloneable` is error-prone (it is shallow by default, bypasses constructors and has an awkward contract), so a copy constructor or explicit copy method is generally preferred.
5. **Optional prototype registry.** A map of named, pre-configured prototypes that clients can look up and copy.
6. **Client code that calls the copy method.** It never uses `new` on the concrete class.

### Use when

- Object creation is costly and copying is cheaper.
- You need many objects that differ only slightly from a template.
- The concrete type is not known at compile time but the object can still be copied.

### Pros and cons

| Pros | Cons |
|---|---|
| Faster than rebuilding complex objects | Deep copying objects with circular references is tricky |
| Hides concrete classes from the client | Every class must implement its own copy logic |
| Avoids large hierarchies of subclasses or factories | Shallow copies can cause shared-state bugs |
| Easy to create pre-configured templates | `Cloneable` / `clone()` has known pitfalls in Java |

---

## Quick Comparison

| Pattern | Problem it solves | Key idea | Must-have element | Spring Boot equivalent |
|---|---|---|---|---|
| Builder | Many or optional constructor parameters | Step-by-step fluent construction, then `build()` | Private product constructor plus static nested Builder | Lombok `@Builder` |
| Factory | Choosing which concrete class to create | Central creation behind an interface | Common interface plus a factory returning it | Injecting a map of beans |
| Singleton | Exactly one shared instance | Controlled access to one object | Private constructor plus static accessor | Default bean scope |
| Abstract Factory | Creating families of related objects that must match | One factory per family, each producing a full set | Abstract factory interface plus one concrete factory per family | Profile- or configuration-based bean selection |
| Prototype | Costly or repetitive object creation | Copy an existing object instead of building a new one | Copy method plus a deliberate deep or shallow copy | Prototype-scoped beans |