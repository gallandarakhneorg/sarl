# Annotation Type

[:Outline:]

An annotation is a form of syntactic metadata that can be added to SARL source code.
Annotations can be reflective in that they can be embedded in binary files generated by the
SARL compiler, and may be retained by the Virtual Machine to be made retrievable at run-time.


## Define an Annotation

For defining an annotation, you could use the [:annotationkw:] keyword.

The following example defines the annotation [:annotationtype:].
This annotation defines three parameters:

* [:valuefield:], an array of strings of characters, without default value;
* [:istrickyfield:], a boolean value, with the default [:falsekw:];
* [:lotterynumberfield:], an array of integer numbers, with a default value.

Examples:

[:Success:]
	package io.sarl.docs.reference.oop
	[:On]
	[:annotationkw](annotation) [:annotationtype](MyAnnotation) {
	  val [:valuefield](value) : String[]
	  val [:istrickyfield](isTricky) : boolean = [:falsekw](false)
	  val [:lotterynumberfield](lotteryNumbers) : int[] = #[ 42, 137 ]
	}
[:End:]


## Modifiers

Modifiers are used to modify declarations of types and type members.
This section introduces the modifiers for the annotation types.
The modifiers are usually written before the keyword for defining the annotation type.

The complete description of the modifiers' semantic is available on [this page](./Modifiers.md).

### Top Annotation Type Modifiers

A top annotation type may be declared with one or more modifiers, which affect its runtime behavior:

* Access modifiers:
	* [:publicmodifier:]: the annotation type is accessible from any other type (default);
	* [:packagemodifier:]: the annotation type is accessible from only the types in the same package.
* [:abstractmodifier:]: the annotation type is abstract and cannot be instanced.

Examples:

[:Success:]
	package io.sarl.docs.reference.oop
	[:On]
	[:publicmodifier](public) annotation TopAnnotationType1 {
	}
	[:packagemodifier](package) annotation TopAnnotationType2 {
	}
	[:abstractmodifier](abstract) annotation TopAnnotationType3 {
	}
[:End:]


### Nested Annotation Type Modifiers

A nested annotation type may be declared with one or more modifiers, which affect its runtime behavior:

* Access modifiers:
	* [:publicmodifier:]: there are no restrictions on accessing the annotation type (default);
	* [:protectedmodifier:]: the annotation type is accessible within the same package, and derived classes;
	* [:packagemodifier:]: the annotation type is accessible only within the same package as its class;
	* [:privatemodifier:]: the annotation type is accessible only within its class.
* [:abstractmodifier:]: the annotation type is abstract and cannot be instanced.
* [:staticmodifier:]: the inner annotation type do not have access to the non-static members of the enclosing type.

> **_Terminology:_** Nested annotation types are divided into two categories: static and non-static.
> Nested annotation types that are declared static are called **static nested annotation types**.
> Non-static nested annotation types are called**inner annotation types**.

> **_Note:_** The modifiers may differ from the previously described, depending on the enclosing type, e.g. agent.

[:Success:]
	package io.sarl.docs.reference.oop
	[:On]
	class EnclosingClass {
		public annotation NestedAnnotationType1 {
		}
		[:protectedmodifier](protected) annotation NestedAnnotationType2 {
		}
		package annotation NestedAnnotationType3 {
		}
		[:privatemodifier](private) annotation NestedAnnotationType4 {
		}
		abstract annotation NestedAnnotationType5 {
		}
		[:staticmodifier](static) annotation NestedAnnotationType6 {
		}
	}
[:End:]


### Value Modifiers

The modifiers for the values in an annotation type are:

* Access modifiers:
	* [:publicmodifier:]: there are no restrictions on accessing the value;
* [:staticmodifier:]: the value is a class value, not an instance value.

Examples:

[:Success:]
	package io.sarl.docs.reference.oop
	annotation MyAnnotationType1 {
	[:On]
		public val val1 : int
		static val val2 : int
	[:Off]
	}
[:End:]

[:Include:](../../includes/oopref.inc)
[:Include:](../../includes/legal.inc)

