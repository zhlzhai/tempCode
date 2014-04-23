package designPattern.collection.copy;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 1. 利用数组来进行存储
 * 2. 每次增长的时候，如果capacityIncrement没有指定的值，会双倍的增长
 * 3. 没有指定的时候，Vector的默认长度为10，自动增长量为0，double增长模式
 * 4. Vector“synchronized”的，vector是线程同步的（疑问：线程同步是线程安全的吗？）这个也是Vector和ArrayList的唯一的区别
 * 5. 它的存储的基础在于数组，所以在插入 ，删除元素的时候需要挪动的元素比较的多，取元素的时候比较的简单
 * **/

public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    
	/**
	 * 存储向量组件的数组缓冲区。vector 的容量就是此数据缓冲区的长度，
	 * 该长度至少要足以包含向量的所有元素。
     * Vector 中的最后一个元素后的任何数组元素都为 null。 
	 * */
    protected Object[] elementData;

    /**
     * 存储时的长度
     * Vector 对象中的有效组件数。
     * 从 elementData[0] 到 elementData[elementCount-1] 的组件均为实际项。
     */
    protected int elementCount;

    /**
     * 向量的大小大于其容量时，容量自动增加的量。
     * 如果容量的增量小于或等于零，则每次需要增大容量时，向量的容量将增大一倍。
     */
    protected int capacityIncrement;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -2767605614048989439L;

    /**
     * 构建方法
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
     * 没有指定的时候，自动增长量为零
     */
    public Vector(int initialCapacity) {
	this(initialCapacity, 0);
    }

    /**
     * 没有指定的时候，开始的时候为10，自动增长量为零
     */
    public Vector() {
	this(10);
    }

    /**
     * 在原有集合（以数组存储）的基础上面创建Vector
     */
    public Vector(Collection<? extends E> c) {
	elementData = c.toArray();
	elementCount = elementData.length;
	// c.toArray might (incorrectly) not return Object[] (see 6260652)
	if (elementData.getClass() != Object[].class)
	    elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
    }

    /**
     * 同步的
     */
    public synchronized void copyInto(Object[] anArray) {
	System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    /**
     * 对此向量的容量削减至此向量的当前大小。对这个方法的应用场合表示疑问？
     */
    public synchronized void trimToSize() {
	modCount++;//已从结构上修改 此列表的次数。
	int oldCapacity = elementData.length;
	if (elementCount < oldCapacity) {
            elementData = Arrays.copyOf(elementData, elementCount);
	}
    }

    /**
     * 增加此向量的容量（如有必要），以确保其至少能够保存最小容量参数指定的组件数。
     */
    public synchronized void ensureCapacity(int minCapacity) {
	modCount++;
	ensureCapacityHelper(minCapacity);
    }

	/**
	 * 如果此向量的当前容量小于 minCapacity，则通过将其内部数据数组 （保存在字段 elementData
	 * 中）替换为一个较大的数组来增加其容量。 新数据数组的大小将为原来的大小加上 capacityIncrement， 除非
	 * capacityIncrement 的值小于或等于零，在后一种情况下， 新的容量将为原来容量的两倍，不过，如果此大小仍然小于
	 * minCapacity， 则新容量将为 minCapacity。
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
     *  newSize如果大于当前的size增加容量，如果小于当前的size，后面的部分直接的置null
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
     * 返回此向量的当前容量
     */
    public synchronized int capacity() {
	return elementData.length;
    }

    /**
     * 返回此向量中的组件数。
     */
    public synchronized int size() {
	return elementCount;
    }

    /**
     * 是否含有组件数
     */
    public synchronized boolean isEmpty() {
	return elementCount == 0;
    }

    /**
     * 返回此向量的组件的枚举。
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
     *  测试指定的对象是否为此向量中的组件。
     */
    public boolean contains(Object o) {
	return indexOf(o, 0) >= 0;
    }

    /**
     *  测试指定的对象在向量中的位置
     */
    public int indexOf(Object o) {
	return indexOf(o, 0);
    }

    /**
     * 搜索给定参数的第一个匹配项，从 index 处开始搜索，并使用 equals 方法测试其相等性。
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
     *返回指定的对象在此向量中最后一个匹配项的索引。
     */
    public synchronized int lastIndexOf(Object o) {
	return lastIndexOf(o, elementCount-1);
    }

    /**
     * 向后搜索指定的对象，从指定的索引处开始，并返回其对应的某个索引。
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
     * 返回指定位置的组件
     */
    public synchronized E elementAt(int index) {
	if (index >= elementCount) {
	    throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
	}

        return (E)elementData[index];
    }

    /**
     * 返回此向量的第一个组件（位于索引 0 处的项）。
     */
    public synchronized E firstElement() {
	if (elementCount == 0) {
	    throw new NoSuchElementException();
	}
	return (E)elementData[0];
    }

    /**
     * 返回此向量的最后一个组件（位于索引 0 处的项）。
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
