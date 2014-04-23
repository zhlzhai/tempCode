package designPattern.collection.copy;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 1. �������������д洢
 * 2. ÿ��������ʱ�����capacityIncrementû��ָ����ֵ����˫��������
 * 3. û��ָ����ʱ��Vector��Ĭ�ϳ���Ϊ10���Զ�������Ϊ0��double����ģʽ
 * 4. Vector��synchronized���ģ�vector���߳�ͬ���ģ����ʣ��߳�ͬ�����̰߳�ȫ���𣿣����Ҳ��Vector��ArrayList��Ψһ������
 * 5. ���Ĵ洢�Ļ����������飬�����ڲ��� ��ɾ��Ԫ�ص�ʱ����ҪŲ����Ԫ�رȽϵĶ࣬ȡԪ�ص�ʱ��Ƚϵļ�
 * **/

public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    
	/**
	 * �洢������������黺������vector ���������Ǵ����ݻ������ĳ��ȣ�
	 * �ó�������Ҫ���԰�������������Ԫ�ء�
     * Vector �е����һ��Ԫ�غ���κ�����Ԫ�ض�Ϊ null�� 
	 * */
    protected Object[] elementData;

    /**
     * �洢ʱ�ĳ���
     * Vector �����е���Ч�������
     * �� elementData[0] �� elementData[elementCount-1] �������Ϊʵ���
     */
    protected int elementCount;

    /**
     * �����Ĵ�С����������ʱ�������Զ����ӵ�����
     * �������������С�ڻ�����㣬��ÿ����Ҫ��������ʱ������������������һ����
     */
    protected int capacityIncrement;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -2767605614048989439L;

    /**
     * ��������
     */
    public Vector(int initialCapacity, int capacityIncrement) {
	super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
	this.elementData = new Object[initialCapacity];
	this.capacityIncrement = capacityIncrement;
    }

    /**
     * û��ָ����ʱ���Զ�������Ϊ��
     */
    public Vector(int initialCapacity) {
	this(initialCapacity, 0);
    }

    /**
     * û��ָ����ʱ�򣬿�ʼ��ʱ��Ϊ10���Զ�������Ϊ��
     */
    public Vector() {
	this(10);
    }

    /**
     * ��ԭ�м��ϣ�������洢���Ļ������洴��Vector
     */
    public Vector(Collection<? extends E> c) {
	elementData = c.toArray();
	elementCount = elementData.length;
	// c.toArray might (incorrectly) not return Object[] (see 6260652)
	if (elementData.getClass() != Object[].class)
	    elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
    }

    /**
     * ͬ����
     */
    public synchronized void copyInto(Object[] anArray) {
	System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    /**
     * �Դ������������������������ĵ�ǰ��С�������������Ӧ�ó��ϱ�ʾ���ʣ�
     */
    public synchronized void trimToSize() {
	modCount++;//�Ѵӽṹ���޸� ���б�Ĵ�����
	int oldCapacity = elementData.length;
	if (elementCount < oldCapacity) {
            elementData = Arrays.copyOf(elementData, elementCount);
	}
    }

    /**
     * ���Ӵ����������������б�Ҫ������ȷ���������ܹ�������С��������ָ�����������
     */
    public synchronized void ensureCapacity(int minCapacity) {
	modCount++;
	ensureCapacityHelper(minCapacity);
    }

	/**
	 * ����������ĵ�ǰ����С�� minCapacity����ͨ�������ڲ��������� ���������ֶ� elementData
	 * �У��滻Ϊһ���ϴ�������������������� ����������Ĵ�С��Ϊԭ���Ĵ�С���� capacityIncrement�� ����
	 * capacityIncrement ��ֵС�ڻ�����㣬�ں�һ������£� �µ�������Ϊԭ������������������������˴�С��ȻС��
	 * minCapacity�� ����������Ϊ minCapacity��
	 */
	private void ensureCapacityHelper(int minCapacity) {
		int oldCapacity = elementData.length;
		if (minCapacity > oldCapacity) {
			Object[] oldData = elementData;
			int newCapacity = (capacityIncrement > 0) ? (oldCapacity + capacityIncrement)
					: (oldCapacity * 2);
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
	}

    /**
     *  newSize������ڵ�ǰ��size�������������С�ڵ�ǰ��size������Ĳ���ֱ�ӵ���null
     */
    public synchronized void setSize(int newSize) {
	modCount++;
	if (newSize > elementCount) {
	    ensureCapacityHelper(newSize);
	} else {
	    for (int i = newSize ; i < elementCount ; i++) {
		elementData[i] = null;
	    }
	}
	elementCount = newSize;
    }

    /**
     * ���ش������ĵ�ǰ����
     */
    public synchronized int capacity() {
	return elementData.length;
    }

    /**
     * ���ش������е��������
     */
    public synchronized int size() {
	return elementCount;
    }

    /**
     * �Ƿ��������
     */
    public synchronized boolean isEmpty() {
	return elementCount == 0;
    }

    /**
     * ���ش������������ö�١�
     */
    public Enumeration<E> elements() {
	return new Enumeration<E>() {
	    int count = 0;

	    public boolean hasMoreElements() {
		return count < elementCount;
	    }

	    public E nextElement() {
		synchronized (Vector.this) {
		    if (count < elementCount) {
			return (E)elementData[count++];
		    }
		}
		throw new NoSuchElementException("Vector Enumeration");
	    }
	};
    }

    /**
     *  ����ָ���Ķ����Ƿ�Ϊ�������е������
     */
    public boolean contains(Object o) {
	return indexOf(o, 0) >= 0;
    }

    /**
     *  ����ָ���Ķ����������е�λ��
     */
    public int indexOf(Object o) {
	return indexOf(o, 0);
    }

    /**
     * �������������ĵ�һ��ƥ����� index ����ʼ��������ʹ�� equals ��������������ԡ�
     */
    public synchronized int indexOf(Object o, int index) {
	if (o == null) {
	    for (int i = index ; i < elementCount ; i++)
		if (elementData[i]==null)
		    return i;
	} else {
	    for (int i = index ; i < elementCount ; i++)
		if (o.equals(elementData[i]))
		    return i;
	}
	return -1;
    }

    /**
     *����ָ���Ķ����ڴ����������һ��ƥ�����������
     */
    public synchronized int lastIndexOf(Object o) {
	return lastIndexOf(o, elementCount-1);
    }

    /**
     * �������ָ���Ķ��󣬴�ָ������������ʼ�����������Ӧ��ĳ��������
     */
    public synchronized int lastIndexOf(Object o, int index) {
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);

	if (o == null) {
	    for (int i = index; i >= 0; i--)
		if (elementData[i]==null)
		    return i;
	} else {
	    for (int i = index; i >= 0; i--)
		if (o.equals(elementData[i]))
		    return i;
	}
	return -1;
    }

    /**
     * ����ָ��λ�õ����
     */
    public synchronized E elementAt(int index) {
	if (index >= elementCount) {
	    throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
	}

        return (E)elementData[index];
    }

    /**
     * ���ش������ĵ�һ�������λ������ 0 �������
     */
    public synchronized E firstElement() {
	if (elementCount == 0) {
	    throw new NoSuchElementException();
	}
	return (E)elementData[0];
    }

    /**
     * ���ش����������һ�������λ������ 0 �������
     */
    public synchronized E lastElement() {
	if (elementCount == 0) {
	    throw new NoSuchElementException();
	}
	return (E)elementData[elementCount - 1];
    }

    /**
     * 
     */
    public synchronized void setElementAt(E obj, int index) {
	if (index >= elementCount) {
	    throw new ArrayIndexOutOfBoundsException(index + " >= " +
						     elementCount);
	}
	elementData[index] = obj;
    }

   
    public synchronized void removeElementAt(int index) {
	modCount++;
	if (index >= elementCount) {
	    throw new ArrayIndexOutOfBoundsException(index + " >= " +
						     elementCount);
	}
	else if (index < 0) {
	    throw new ArrayIndexOutOfBoundsException(index);
	}
	int j = elementCount - index - 1;
	if (j > 0) {
	    System.arraycopy(elementData, index + 1, elementData, index, j);
	}
	elementCount--;
	elementData[elementCount] = null; /* to let gc do its work */
    }

    
    public synchronized void insertElementAt(E obj, int index) {
	modCount++;
	if (index > elementCount) {
	    throw new ArrayIndexOutOfBoundsException(index
						     + " > " + elementCount);
	}
	ensureCapacityHelper(elementCount + 1);
	System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
	elementData[index] = obj;
	elementCount++;
    }

   
    public synchronized void addElement(E obj) {
	modCount++;
	ensureCapacityHelper(elementCount + 1);
	elementData[elementCount++] = obj;
    }

   
    public synchronized boolean removeElement(Object obj) {
	modCount++;
	int i = indexOf(obj);
	if (i >= 0) {
	    removeElementAt(i);
	    return true;
	}
	return false;
    }

    
    public synchronized void removeAllElements() {
        modCount++;
	// Let gc do its work
	for (int i = 0; i < elementCount; i++)
	    elementData[i] = null;

	elementCount = 0;
    }

  
    public synchronized Object clone() {
	try {
	    Vector<E> v = (Vector<E>) super.clone();
	    v.elementData = Arrays.copyOf(elementData, elementCount);
	    v.modCount = 0;
	    return v;
	} catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    
    public synchronized Object[] toArray() {
        return Arrays.copyOf(elementData, elementCount);
    }

    
    public synchronized <T> T[] toArray(T[] a) {
        if (a.length < elementCount)
            return (T[]) Arrays.copyOf(elementData, elementCount, a.getClass());

	    System.arraycopy(elementData, 0, a, 0, elementCount);

        if (a.length > elementCount)
            a[elementCount] = null;

        return a;
    }

   
    public synchronized E get(int index) {
	if (index >= elementCount)
	    throw new ArrayIndexOutOfBoundsException(index);

	return (E)elementData[index];
    }

  
    public synchronized E set(int index, E element) {
	if (index >= elementCount)
	    throw new ArrayIndexOutOfBoundsException(index);

	Object oldValue = elementData[index];
	elementData[index] = element;
	return (E)oldValue;
    }

  
    public synchronized boolean add(E e) {
	modCount++;
	ensureCapacityHelper(elementCount + 1);
	elementData[elementCount++] = e;
        return true;
    }

    
    public boolean remove(Object o) {
        return removeElement(o);
    }

   
    public void add(int index, E element) {
        insertElementAt(element, index);
    }

   
    public synchronized E remove(int index) {
	modCount++;
	if (index >= elementCount)
	    throw new ArrayIndexOutOfBoundsException(index);
	Object oldValue = elementData[index];

	int numMoved = elementCount - index - 1;
	if (numMoved > 0)
	    System.arraycopy(elementData, index+1, elementData, index,
			     numMoved);
	elementData[--elementCount] = null; // Let gc do its work

	return (E)oldValue;
    }

   
    public void clear() {
        removeAllElements();
    }

   
    public synchronized boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

   
    public synchronized boolean addAll(Collection<? extends E> c) {
	modCount++;
        Object[] a = c.toArray();
        int numNew = a.length;
	ensureCapacityHelper(elementCount + numNew);
        System.arraycopy(a, 0, elementData, elementCount, numNew);
        elementCount += numNew;
	return numNew != 0;
    }

    
    public synchronized boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

  
    public synchronized boolean retainAll(Collection<?> c)  {
        return super.retainAll(c);
    }

   
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
	modCount++;
	if (index < 0 || index > elementCount)
	    throw new ArrayIndexOutOfBoundsException(index);

        Object[] a = c.toArray();
	int numNew = a.length;
	ensureCapacityHelper(elementCount + numNew);

	int numMoved = elementCount - index;
	if (numMoved > 0)
	    System.arraycopy(elementData, index, elementData, index + numNew,
			     numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
	elementCount += numNew;
	return numNew != 0;
    }

   
    public synchronized boolean equals(Object o) {
        return super.equals(o);
    }

   
    public synchronized int hashCode() {
        return super.hashCode();
    }

   
    public synchronized String toString() {
        return super.toString();
    }

    

    protected synchronized void removeRange(int fromIndex, int toIndex) {
	modCount++;
	int numMoved = elementCount - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

	// Let gc do its work
	int newElementCount = elementCount - (toIndex-fromIndex);
	while (elementCount != newElementCount)
	    elementData[--elementCount] = null;
    }

   
    private synchronized void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException
    {
	s.defaultWriteObject();
    }
}
