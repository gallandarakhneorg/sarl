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
package io.sarl.bspl.lang.sarl_bspl.impl;

import io.sarl.bspl.lang.sarl_bspl.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Sarl_bsplFactoryImpl extends EFactoryImpl implements Sarl_bsplFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Sarl_bsplFactory init()
	{
		try
		{
			Sarl_bsplFactory theSarl_bsplFactory = (Sarl_bsplFactory)EPackage.Registry.INSTANCE.getEFactory(Sarl_bsplPackage.eNS_URI);
			if (theSarl_bsplFactory != null)
			{
				return theSarl_bsplFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Sarl_bsplFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sarl_bsplFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case Sarl_bsplPackage.BSPL_PROTOCOL_SPECIFICATION: return createBsplProtocolSpecification();
			case Sarl_bsplPackage.BSPL_PROTOCOL: return createBsplProtocol();
			case Sarl_bsplPackage.BSPL_PROTOCOL_MEMBER: return createBsplProtocolMember();
			case Sarl_bsplPackage.BSPL_PROTOCOL_ARGUMENT: return createBsplProtocolArgument();
			case Sarl_bsplPackage.BSPL_PROTOCOL_ROLE: return createBsplProtocolRole();
			case Sarl_bsplPackage.BSPL_PROTOCOL_PARAMETER: return createBsplProtocolParameter();
			case Sarl_bsplPackage.BSPL_PROTOCOL_MESSAGE: return createBsplProtocolMessage();
			case Sarl_bsplPackage.BSPL_PROTOCOL_PROTOCOL_CALL: return createBsplProtocolProtocolCall();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolSpecification createBsplProtocolSpecification()
	{
		BsplProtocolSpecificationImpl bsplProtocolSpecification = new BsplProtocolSpecificationImpl();
		return bsplProtocolSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocol createBsplProtocol()
	{
		BsplProtocolImplCustom bsplProtocol = new BsplProtocolImplCustom();
		return bsplProtocol;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolMember createBsplProtocolMember()
	{
		BsplProtocolMemberImplCustom bsplProtocolMember = new BsplProtocolMemberImplCustom();
		return bsplProtocolMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolArgument createBsplProtocolArgument()
	{
		BsplProtocolArgumentImplCustom bsplProtocolArgument = new BsplProtocolArgumentImplCustom();
		return bsplProtocolArgument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolRole createBsplProtocolRole()
	{
		BsplProtocolRoleImplCustom bsplProtocolRole = new BsplProtocolRoleImplCustom();
		return bsplProtocolRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolParameter createBsplProtocolParameter()
	{
		BsplProtocolParameterImplCustom bsplProtocolParameter = new BsplProtocolParameterImplCustom();
		return bsplProtocolParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolMessage createBsplProtocolMessage()
	{
		BsplProtocolMessageImplCustom bsplProtocolMessage = new BsplProtocolMessageImplCustom();
		return bsplProtocolMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BsplProtocolProtocolCall createBsplProtocolProtocolCall()
	{
		BsplProtocolProtocolCallImpl bsplProtocolProtocolCall = new BsplProtocolProtocolCallImpl();
		return bsplProtocolProtocolCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Sarl_bsplPackage getSarl_bsplPackage()
	{
		return (Sarl_bsplPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Sarl_bsplPackage getPackage()
	{
		return Sarl_bsplPackage.eINSTANCE;
	}

} //Sarl_bsplFactoryImpl
