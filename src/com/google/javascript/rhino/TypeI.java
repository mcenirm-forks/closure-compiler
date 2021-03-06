/*
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Ben Lickly
 *   Dimitris Vardoulakis
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package com.google.javascript.rhino;

import java.io.Serializable;

/**
 * A common interface for types in the old type system and the new type system,
 * so that the other passes need not know which type system they are using.
 *
 * @author blickly@google.com (Ben Lickly)
 * @author dimvar@google.com (Dimitris Vardoulakis)
 */
public interface TypeI extends Serializable {

  boolean isBottom();

  boolean isTop();

  boolean isTypeVariable();

  boolean isUnresolved();

  boolean isBoxableScalar();

  TypeI autobox();

  // Hacky method to abstract away corner case handling of the way OTI
  // represents unresolved types.
  boolean isUnresolvedOrResolvedUnknown();

  boolean isConstructor();

  boolean isEquivalentTo(TypeI type);

  boolean isFunctionType();

  /**
   * @return True for both nominal and structural interfaces
   */
  boolean isInterface();

  boolean isStructuralInterface();

  boolean isSubtypeOf(TypeI type);

  boolean isSubtypeWithoutStructuralTyping(TypeI type);

  boolean containsArray();

  boolean isUnknownType();

  boolean isSomeUnknownType();

  boolean isObjectType();

  /**
   * True if this type represents a generic object (non function) type, instantiated or not.
   */
  boolean isGenericObjectType();

  /**
   * True when the nominal type of this type is Object. The name is not great, because objects
   * with a different nominal type can flow to places that treat them as Object. But I'm not
   * sure what a better name would be.
   */
  boolean isInstanceofObject();

  boolean isUnionType();

  boolean isNullable();

  boolean isVoidable();

  boolean isNullType();

  boolean isVoidType();

  boolean isPrototypeObject();

  boolean isLiteralObject();

  boolean isEnumElement();

  // TODO(sdh): When OTI is gone, these can be renamed to simply isString and isNumber
  // (provided we have sufficiently clear JavaDoc to specify that it does *not* include
  // the object wrapper type).
  /**
   * Whether the type is a scalar string. In OTI, the isString method returns true for String
   * objects as well.
   */
  boolean isStringValueType();

  /**
   * Whether this type represents an anonymous structural type, e.g., { a: number, b: string }.
   * Returns false for named structural types (i.e., types defined using @record).
   */
  boolean isRecordType();

  /**
   * Whether the type is a scalar number. In OTI, the isNumber method returns true for Number
   * objects as well.
   */
  boolean isNumberValueType();

  /**
   * Whether the type is a scalar boolean. In OTI, the isBoolean method returns true for Boolean
   * objects as well.
   */
  boolean isBooleanValueType();

  ObjectTypeI autoboxAndGetObject();

  JSDocInfo getJSDocInfo();

  /**
   * If this is a union type, returns a union type that does not include
   * the null or undefined type.
   */
  TypeI restrictByNotNullOrUndefined();

  /**
   * Downcasts this to a FunctionTypeI, or returns null if this is not
   * a function.
   */
  FunctionTypeI toMaybeFunctionType();

  /**
   * If this type is a single object, downcast it to ObjectTypeI.
   * If it is a non-object or a union of objects, return null.
   */
  ObjectTypeI toMaybeObjectType();

  /**
   * If this type is a union type, returns a list of its members. Otherwise
   * returns null.
   */
  Iterable<? extends TypeI> getUnionMembers();

  TypeI meetWith(TypeI other);

  String getDisplayName();

  TypeI getGreatestSubtypeWithProperty(String propName);

  TypeI getEnumeratedTypeOfEnumElement();

  boolean isEnumObject();

  /**
   * Returns true if this type is a generic object (non function) and some (or all) of its type
   * variables are not instantiated.
   * Note that in OTI it is possible to instantiate only some of the type variables of a
   * generic type (bad implementation choice).
   * In NTI, a generic type can only be uninstantiated or fully instantiated.
   */
  boolean hasUninstantiatedTypeVariables();

  // TODO(sdh): Replace calls of toAnnotationString with toNonNullAnnotationString and
  // then substring off any leading '!' if necessary.  Then delete toAnnotationString
  // and consider renaming toNonNullAnnotationString as simply toAnnotationString.
  /**
   * Returns a string representation of this type, suitable for printing
   * in type annotations at code generation time.  In particular, explicit
   * non-null modifiers will be added to implicitly nullable types (except
   * the outermost type, which is expected to be a reference type).
   */
  String toAnnotationString();

  /**
   * Returns a string representation of this type, suitable for printing
   * in type annotations at code generation time.  In particular, explicit
   * non-null modifiers will be added to implicitly nullable types.
   */
  String toNonNullAnnotationString();
}
