package com.sun.mdm.index.dataobject;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;




/**
 * @author Sujit Biswas
 *
 */
public class DataObject1Test extends TestCase {

	private Mockery context = new Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	public Class<DataObject> getTargetClass() {
		return DataObject.class;
	}

	public void testConstructor() throws Throwable {
		DataObject dataObject = new DataObject();
		assertEquals("dataObject.fields.size()", 0, dataObject.fields.size());
		assertEquals("dataObject.childTypes.size()", 0, dataObject.childTypes
				.size());
	}

	public void testAdd() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.add(0, "testDataObjectElement");
		assertEquals("dataObject.fields.size()", 1, dataObject.fields.size());
		assertEquals("dataObject.fields.get(0)", "testDataObjectElement",
				dataObject.fields.get(0));
	}

	public void testAddChild() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(100, new DataObject());
		assertEquals("dataObject.childTypes.size()", 101, dataObject.childTypes
				.size());
	}

	public void testAddChild1() throws Throwable {
		DataObject dataObject = new DataObject();
		DataObject child = new DataObject();
		dataObject.addChild(0, child);
		dataObject.addChild(0, child);
		Integer index = new Integer(0);
		assertEquals("dataObject.childTypes.get(0).getChildren().size()", 2,
				((ChildType) dataObject.childTypes.get(index.intValue()))
						.getChildren().size());
		assertSame("dataObject.childTypes.get(0).getChildren().get(0)", child,
				((ChildType) dataObject.childTypes.get(index.intValue()))
						.getChildren().get(0));
	}

	public void testAddChildType() throws Throwable {
		DataObject dataObject = new DataObject();
		ChildType o = new ChildType();
		boolean result = dataObject.addChildType(o);
		assertEquals("dataObject.childTypes.size()", 1, dataObject.childTypes
				.size());
		assertSame("dataObject.childTypes.get(0)", o, dataObject.childTypes
				.get(0));
		assertTrue("result", result);
	}

	public void testAddFieldValue() throws Throwable {
		DataObject dataObject = new DataObject();
		boolean result = dataObject.addFieldValue("testDataObjecto");
		assertEquals("dataObject.fields.size()", 1, dataObject.fields.size());
		assertEquals("dataObject.fields.get(0)", "testDataObjecto",
				dataObject.fields.get(0));
		assertTrue("result", result);
	}

	public void testEnsureCapacity() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addFieldValue("testDataObjecto");
		assertEquals("dataObject.fields.size()", 1, dataObject.fields.size());
	}

	public void testEnsureChildType() throws Throwable {
		final DataObject dataObject = context.mock(DataObject.class);

		// expectations
		context.checking(new Expectations() {
			{
				one(dataObject).getChildTypes().get(0);
				one(dataObject).addChildType(null);

			}
		});

		dataObject.addChildType(null);
		dataObject.getChildTypes().get(0);

		context.assertIsSatisfied();

	}

	public void testEnsureChildType1() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(100, new DataObject());
		ChildType result = dataObject.getChildTypes().get(100);
		assertEquals("result.getChildren().size()", 1, result.getChildren()
				.size());
	}

	public void testGetChild() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(1, new DataObject());
		dataObject.addChild(1, null);
		DataObject result = dataObject.getChild(1, 1);
		assertNull("result", result);
	}

	public void testGetChildTypes() throws Throwable {
		DataObject dataObject = new DataObject();
		// ChildType childType = (ChildType)
		// callPrivateMethod("com.sun.mdm.index.dataobject.DataObject",
		// "ensureChildType", new Class[] {int.class}, dataObject, new Object[]
		// {new Integer(100)});
		ArrayList result = dataObject.getChildTypes();
		assertEquals("result.size()", 0, result.size());
		// assertSame("result.get(100)", childType, result.get(100));
	}

	public void testGetChildTypes1() throws Throwable {
		ArrayList result = new DataObject().getChildTypes();
		assertEquals("result.size()", 0, result.size());
	}

	public void testGetChildren() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(4, new DataObject());
		ArrayList result = (ArrayList) dataObject.getChildren(0);
		assertEquals("result.size()", 0, result.size());
	}

	public void testGetFieldValue() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.setFieldValue(1, "testDataObjectFieldValue");
		String result = dataObject.getFieldValue(1);
		assertEquals("result", "testDataObjectFieldValue", result);
	}

	public void testGetFieldValue1() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.setFieldValue(100, "testDataObjectParam2");
		String result = dataObject.getFieldValue(0);
		assertNull("result", result);
	}

	public void testGetFieldValues() throws Throwable {
		ArrayList result = new DataObject().getFieldValues();
		assertEquals("result.size()", 0, result.size());
	}

	public void testGetFieldValues1() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addFieldValue("testDataObjecto");
		ArrayList result = dataObject.getFieldValues();
		assertEquals("result.size()", 1, result.size());
		assertEquals("result.get(0)", "testDataObjecto", result.get(0));
	}

	public void testHasChild() throws Throwable {
		boolean result = new DataObject().hasChild(1);
		assertFalse("result", result);
	}

	public void testHasChild1() throws Throwable {
		boolean result = new DataObject().hasChild(-1);
		assertFalse("result", result);
	}

	public void testHasChild2() throws Throwable {
		boolean result = new DataObject().hasChild(0);
		assertFalse("result", result);
	}

	public void testHasChild4() throws Throwable {
		DataObject dataObject = new DataObject();
		boolean result = dataObject.hasChild(0);
		assertFalse("result", result);
	}

	public void testHasField() throws Throwable {
		DataObject dataObject = new DataObject();

		dataObject.setFieldValue(10, "");
		boolean result = dataObject.hasField(0);
		assertTrue("result", result);
	}

	public void testHasField1() throws Throwable {
		boolean result = new DataObject().hasField(-1);
		assertFalse("result", result);
	}

	public void testHasField2() throws Throwable {
		boolean result = new DataObject().hasField(0);
		assertFalse("result", result);
	}

	public void testSetChild() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(10, new DataObject());
		dataObject.setChild(10, new DataObject(), 0);
		assertEquals("dataObject.childTypes.size()", 11, dataObject.childTypes
				.size());
	}

	public void testSetFieldValue() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.setFieldValue(10, "testDataObjectParam2");
		String result = dataObject.setFieldValue(10, "testDataObjectParam2");
		assertEquals("result", "testDataObjectParam2", result);
		assertEquals("dataObject.fields.size()", 11, dataObject.fields.size());
	}

	public void testSetFieldValue1() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.setFieldValue(100, "testDataObjectParam2");
		String result = dataObject.setFieldValue(0, "testDataObjectParam2");
		assertEquals("dataObject.fields.get(0)", "testDataObjectParam2",
				dataObject.fields.get(0));
		assertNull("result", result);
		assertEquals("dataObject.fields.size()", 101, dataObject.fields.size());
	}

	public void testSetFieldValue2() throws Throwable {
		DataObject dataObject = new DataObject();
		String result = dataObject.setFieldValue(100, "testDataObjectParam2");
		assertEquals("dataObject.fields.size()", 101, dataObject.fields.size());
		assertNull("result", result);
	}

	public void testToString() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addFieldValue("testDataObjecto");
		String result = dataObject.toString();
		assertEquals("result", "fields: \n\tfield: testDataObjecto\n", result);
	}

	public void testToString3() throws Throwable {
		String result = new DataObject().toString();
		assertEquals("result", "fields: \n", result);
	}

	public void testToString4() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(100, new DataObject());
		String result = dataObject.toString();
		assertEquals(
				"result",
				"fields: \nchildTypes: \nchildType-1\nchildType-2\nchildType-3\nchildType-4\nchildType-5\nchildType-6\nchildType-7\nchildType-8\nchildType-9\nchildType-10\nchildType-11\nchildType-12\nchildType-13\nchildType-14\nchildType-15\nchildType-16\nchildType-17\nchildType-18\nchildType-19\nchildType-20\nchildType-21\nchildType-22\nchildType-23\nchildType-24\nchildType-25\nchildType-26\nchildType-27\nchildType-28\nchildType-29\nchildType-30\nchildType-31\nchildType-32\nchildType-33\nchildType-34\nchildType-35\nchildType-36\nchildType-37\nchildType-38\nchildType-39\nchildType-40\nchildType-41\nchildType-42\nchildType-43\nchildType-44\nchildType-45\nchildType-46\nchildType-47\nchildType-48\nchildType-49\nchildType-50\nchildType-51\nchildType-52\nchildType-53\nchildType-54\nchildType-55\nchildType-56\nchildType-57\nchildType-58\nchildType-59\nchildType-60\nchildType-61\nchildType-62\nchildType-63\nchildType-64\nchildType-65\nchildType-66\nchildType-67\nchildType-68\nchildType-69\nchildType-70\nchildType-71\nchildType-72\nchildType-73\nchildType-74\nchildType-75\nchildType-76\nchildType-77\nchildType-78\nchildType-79\nchildType-80\nchildType-81\nchildType-82\nchildType-83\nchildType-84\nchildType-85\nchildType-86\nchildType-87\nchildType-88\nchildType-89\nchildType-90\nchildType-91\nchildType-92\nchildType-93\nchildType-94\nchildType-95\nchildType-96\nchildType-97\nchildType-98\nchildType-99\nchildType-100\nchildType-101\n\tchildInstance: fields: \n\n",
				result);
	}

	public void testToString5() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addFieldValue("testDataObjecto");
		dataObject.addChild(100, new DataObject());
		String result = dataObject.toString();
		assertEquals(
				"result",
				"fields: \n\tfield: testDataObjecto\nchildTypes: \nchildType-1\nchildType-2\nchildType-3\nchildType-4\nchildType-5\nchildType-6\nchildType-7\nchildType-8\nchildType-9\nchildType-10\nchildType-11\nchildType-12\nchildType-13\nchildType-14\nchildType-15\nchildType-16\nchildType-17\nchildType-18\nchildType-19\nchildType-20\nchildType-21\nchildType-22\nchildType-23\nchildType-24\nchildType-25\nchildType-26\nchildType-27\nchildType-28\nchildType-29\nchildType-30\nchildType-31\nchildType-32\nchildType-33\nchildType-34\nchildType-35\nchildType-36\nchildType-37\nchildType-38\nchildType-39\nchildType-40\nchildType-41\nchildType-42\nchildType-43\nchildType-44\nchildType-45\nchildType-46\nchildType-47\nchildType-48\nchildType-49\nchildType-50\nchildType-51\nchildType-52\nchildType-53\nchildType-54\nchildType-55\nchildType-56\nchildType-57\nchildType-58\nchildType-59\nchildType-60\nchildType-61\nchildType-62\nchildType-63\nchildType-64\nchildType-65\nchildType-66\nchildType-67\nchildType-68\nchildType-69\nchildType-70\nchildType-71\nchildType-72\nchildType-73\nchildType-74\nchildType-75\nchildType-76\nchildType-77\nchildType-78\nchildType-79\nchildType-80\nchildType-81\nchildType-82\nchildType-83\nchildType-84\nchildType-85\nchildType-86\nchildType-87\nchildType-88\nchildType-89\nchildType-90\nchildType-91\nchildType-92\nchildType-93\nchildType-94\nchildType-95\nchildType-96\nchildType-97\nchildType-98\nchildType-99\nchildType-100\nchildType-101\n\tchildInstance: fields: \n\n",
				result);
	}

	public void testAddChildThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.addChild(-1, new DataObject());
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

			assertEquals("dataObject.childTypes.size()", 0,
					dataObject.childTypes.size());
		}
	}

	public void testAddChildThrowsNullPointerException() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChildType(null);
		try {
			dataObject.addChild(0, new DataObject());
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());

			assertEquals("dataObject.childTypes.size()", 1,
					dataObject.childTypes.size());
		}
	}

	public void testAddThrowsIndexOutOfBoundsException() throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.add(100, "testDataObjectElement");
			fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "Index: 100, Size: 0", ex
					.getMessage());

			assertEquals("dataObject.fields.size()", 0, dataObject.fields
					.size());
		}
	}

	public void testEnsureChildTypeThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.getChildren(-1);
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

			assertEquals("dataObject.childTypes.size()", 0,
					dataObject.childTypes.size());
		}
	}

	public void testGetChildThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		try {
			new DataObject().getChild(-1, 100);
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

		}
	}

	public void testGetChildThrowsIndexOutOfBoundsException() throws Throwable {
		try {
			new DataObject().getChild(100, 1000);
			fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "Index: 100, Size: 0", ex
					.getMessage());

		}
	}

	public void testGetChildrenThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		try {
			new DataObject().getChildren(-1);
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

		}
	}

	public void testGetChildrenThrowsIndexOutOfBoundsException()
			throws Throwable {
		try {
			new DataObject().getChildren(100);
			fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "Index: 100, Size: 0", ex
					.getMessage());

		}
	}

	public void testGetChildrenThrowsNullPointerException() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(27, new DataObject());
		dataObject.addChildType(null);
		try {
			dataObject.getChildren(28);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());

		}
	}

	public void testGetFieldValueThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.getFieldValue(-1);
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

			assertEquals("dataObject.fields.size()", 0, dataObject.fields
					.size());
		}
	}

	public void testGetFieldValueThrowsIndexOutOfBoundsException()
			throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.getFieldValue(100);
			fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "Index: 100, Size: 0", ex
					.getMessage());

			assertEquals("dataObject.fields.size()", 0, dataObject.fields
					.size());
		}
	}

	public void testSetChildThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.setChild(-1, new DataObject(), 100);
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

			assertEquals("dataObject.childTypes.size()", 0,
					dataObject.childTypes.size());
		}
	}

	public void testSetChildThrowsIndexOutOfBoundsException() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChild(100, new DataObject());
		try {
			dataObject.setChild(100, new DataObject(), 1000);
			fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "Index: 1000, Size: 1", ex
					.getMessage());

			assertEquals("dataObject.childTypes.size()", 101,
					dataObject.childTypes.size());
		}
	}

	public void testSetChildThrowsIndexOutOfBoundsException1() throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.setChild(100, new DataObject(), 1000);
			fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException ex) {
			assertEquals("dataObject.childTypes.size()", 101,
					dataObject.childTypes.size());
			assertEquals("ex.getMessage()", "Index: 1000, Size: 0", ex
					.getMessage());

		}
	}

	public void testSetChildThrowsNullPointerException() throws Throwable {
		DataObject dataObject = new DataObject();
		dataObject.addChildType(null);
		try {
			dataObject.setChild(0, new DataObject(), 100);
			fail("Expected NullPointerException to be thrown");
		} catch (NullPointerException ex) {
			assertNull("ex.getMessage()", ex.getMessage());

			assertEquals("dataObject.childTypes.size()", 1,
					dataObject.childTypes.size());
		}
	}

	public void testSetFieldValueThrowsArrayIndexOutOfBoundsException()
			throws Throwable {
		DataObject dataObject = new DataObject();
		try {
			dataObject.setFieldValue(-1, "testDataObjectFieldValue");
			fail("Expected ArrayIndexOutOfBoundsException to be thrown");
		} catch (ArrayIndexOutOfBoundsException ex) {
			assertEquals("ex.getMessage()", "-1", ex.getMessage());

			assertEquals("dataObject.fields.size()", 0, dataObject.fields
					.size());
		}
	}
}
