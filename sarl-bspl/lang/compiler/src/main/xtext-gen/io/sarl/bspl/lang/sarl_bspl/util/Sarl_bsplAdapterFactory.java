/**
 * $Id$
 * 
 * File is automatically generated by the Xtext language generator.
 * Do not change it.
 * 
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 * 
 * Copyright (C) 2014-2025 SARL.io, the Original Authors and Main Authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.bspl.lang.sarl_bspl.util;

import io.sarl.bspl.lang.sarl_bspl.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see io.sarl.bspl.lang.sarl_bspl.Sarl_bsplPackage
 * @generated
 */
public class Sarl_bsplAdapterFactory extends AdapterFactoryImpl
{
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Sarl_bsplPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sarl_bsplAdapterFactory()
	{
		if (modelPackage == null)
		{
			modelPackage = Sarl_bsplPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object)
	{
		if (object == modelPackage)
		{
			return true;
		}
		if (object instanceof EObject)
		{
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Sarl_bsplSwitch<Adapter> modelSwitch =
		new Sarl_bsplSwitch<Adapter>()
		{
			@Override
			public Adapter caseBsplProtocolSpecification(BsplProtocolSpecification object)
			{
				return createBsplProtocolSpecificationAdapter();
			}
			@Override
			public Adapter caseBsplProtocol(BsplProtocol object)
			{
				return createBsplProtocolAdapter();
			}
			@Override
			public Adapter caseBsplProtocolMember(BsplProtocolMember object)
			{
				return createBsplProtocolMemberAdapter();
			}
			@Override
			public Adapter caseBsplProtocolArgument(BsplProtocolArgument object)
			{
				return createBsplProtocolArgumentAdapter();
			}
			@Override
			public Adapter caseBsplProtocolRole(BsplProtocolRole object)
			{
				return createBsplProtocolRoleAdapter();
			}
			@Override
			public Adapter caseBsplProtocolParameter(BsplProtocolParameter object)
			{
				return createBsplProtocolParameterAdapter();
			}
			@Override
			public Adapter caseBsplProtocolMessage(BsplProtocolMessage object)
			{
				return createBsplProtocolMessageAdapter();
			}
			@Override
			public Adapter caseBsplProtocolProtocolCall(BsplProtocolProtocolCall object)
			{
				return createBsplProtocolProtocolCallAdapter();
			}
			@Override
			public Adapter caseBsplAnnotationTarget(BsplAnnotationTarget object)
			{
				return createBsplAnnotationTargetAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object)
			{
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target)
	{
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolSpecification <em>Bspl Protocol Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolSpecification
	 * @generated
	 */
	public Adapter createBsplProtocolSpecificationAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocol <em>Bspl Protocol</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocol
	 * @generated
	 */
	public Adapter createBsplProtocolAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolMember <em>Bspl Protocol Member</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolMember
	 * @generated
	 */
	public Adapter createBsplProtocolMemberAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolArgument <em>Bspl Protocol Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolArgument
	 * @generated
	 */
	public Adapter createBsplProtocolArgumentAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolRole <em>Bspl Protocol Role</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolRole
	 * @generated
	 */
	public Adapter createBsplProtocolRoleAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolParameter <em>Bspl Protocol Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolParameter
	 * @generated
	 */
	public Adapter createBsplProtocolParameterAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolMessage <em>Bspl Protocol Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolMessage
	 * @generated
	 */
	public Adapter createBsplProtocolMessageAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplProtocolProtocolCall <em>Bspl Protocol Protocol Call</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplProtocolProtocolCall
	 * @generated
	 */
	public Adapter createBsplProtocolProtocolCallAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link io.sarl.bspl.lang.sarl_bspl.BsplAnnotationTarget <em>Bspl Annotation Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see io.sarl.bspl.lang.sarl_bspl.BsplAnnotationTarget
	 * @generated
	 */
	public Adapter createBsplAnnotationTargetAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter()
	{
		return null;
	}

} //Sarl_bsplAdapterFactory
