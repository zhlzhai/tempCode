package designPattern.collection.copy;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.RandomAccess;

/**
 * 1.�����̰߳�ȫ�ģ�������Ϊ�������д洢��Ĭ�ϵĴ�СΪ10�����������б仯ԭ���ģ�size*3��/2+1
 * ���Ա���Ϊԭ��������ռ����2ȡ��������ԭ���Ŀռ��С�ټ�һ
 * 
 * 2.�����Ƚϵ���Ϥ�����Ƿ�������private������
 * 		private void readObject(java.io.ObjectInputStream s)
 * 		private void writeObject(java.io.ObjectOutputStream s)
 * 3. ���ܹ����л�������ݵ���Ϣ������������������������Ժ�ArrayList�ǿ������л��ġ��������Ƚϵ�����˼��
 * */
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    private transient Object[] elementData;

    private int size;

    public ArrayList(int initialCapacity) {
	super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
	this.elementData = new Object[initialCapacity];
    }

    public ArrayList() {
	this(10);
    }

    public ArrayList(Collection<? extends E> c) {
	elementData = c.toArray();
	size = elementData.length;
	// c.toArray might (incorrectly) not return Object[] (see 6260652)
	if (elementData.getClass() != Object[].class)
	    elementData = Arrays.copyOf(elementData, size, Object[].class);
    }

    public void trimToSize() {
	modCount++;
	int oldCapacity = elementData.length;
	if (size < oldCapacity) {
            elementData = Arrays.copyOf(elementData, size);
	}
    }

   
    public void ensureCapacity(int minCapacity) {
	modCount++;
	int oldCapacity = elementData.length;
	if (minCapacity > oldCapacity) {
	    Object oldData[] = elementData;
	    int newCapacity = (oldCapacity * 3)/2 + 1;
    	    if (newCapacity < minCapacity)
		newCapacity = minCapacity;
            // minCapacity is usually close to size, so this is a win:
            elementData = Arrays.copyOf(elementData, newCapacity);
	}
    }

    public int size() {
	return size;
    }

   
    public boolean isEmpty() {
	return size == 0;
    }

    
    public boolean contains(Object o) {
	return indexOf(o) >= 0;
    }

    
    public int indexOf(Object o) {
	if (o == null) {
	    for (int i = 0; i < size; i++)
		if (elementData[i]==null)
		    return i;
	} else {
	    for (int i = 0; i < size; i++)
		if (o.equals(elementData[i]))
		    return i;
	}
	return -1;
    }

    public int lastIndexOf(Object o) {
	if (o == null) {
	    for (int i = size-1; i >= 0; i--)
		if (elementData[i]==null)
		    return i;
	} else {
	    for (int i = size-1; i >= 0; i--)
		if (o.equals(elementData[i]))
		    return i;
	}
	return -1;
    }

    
    public Object clone() {
	try {
	    ArrayList<E> v = (ArrayList<E>) super.clone();
	    v.elementData = Arrays.copyOf(elementData, size);
	    v.modCount = 0;
	    return v;
	} catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

   
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

   
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
	System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    
    public E get(int index) {
	RangeCheck(index);

	return (E) elementData[index];
    }

   
    public E set(int index, E element) {
	RangeCheck(index);

	E oldValue = (E) elementData[index];
	elementData[index] = element;
	return oldValue;
    }

   
    public boolean add(E e) {
	ensureCapacity(size + 1);  // Increments modCount!!
	elementData[size++] = e;
	return true;
    }

   
    public void add(int index, E element) {
	if (index > size || index < 0)
	    throw new IndexOutOfBoundsException(
		"Index: "+index+", Size: "+size);

	ensureCapacity(size+1);  // Increments modCount!!
	System.arraycopy(elementData, index, elementData, index + 1,
			 size - index);
	elementData[index] = element;
	size++;
    }

    
    public E remove(int index) {
	RangeCheck(index);

	modCount++;
	E oldValue = (E) elementData[index];

	int numMoved = size - index - 1;
	if (numMoved > 0)
	    System.arraycopy(elementData, index+1, elementData, index,
			     numMoved);
	elementData[--size] = null; // Let gc do its work

	return oldValue;
    }

   
    public boolean remove(Object o) {
	if (o == null) {
            for (int index = 0; index < size; index++)
		if (elementData[index] == null) {
		    fastRemove(index);
		    return true;
		}
	} else {
	    for (int index = 0; index < size; index++)
		if (o.equals(elementData[index])) {
		    fastRemove(index);
		    return true;
		}
        }
	return false;
    }

   
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // Let gc do its work
    }

    
    public void clear() {
	modCount++;

	// Let gc do its work
	for (int i = 0; i < size; i++)
	    elementData[i] = null;

	size = 0;
    }

    
    public boolean addAll(Collection<? extends E> c) {
	Object[] a = c.toArray();
        int numNew = a.length;
	ensureCapacity(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
	return numNew != 0;
    }

   
    public boolean addAll(int index, Collection<? extends E> c) {
	if (index > size || index < 0)
	    throw new IndexOutOfBoundsException(
		"Index: " + index + ", Size: " + size);

	Object[] a = c.toArray();
	int numNew = a.length;
	ensureCapacity(size + numNew);  // Increments modCount

	int numMoved = size - index;
	if (numMoved > 0)
	    System.arraycopy(elementData, index, elementData, index + numNew,
			     numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
	size += numNew;
	return numNew != 0;
    }

    
    protected void removeRange(int fromIndex, int toIndex) {
	modCount++;
	int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

	// Let gc do its work
	int newSize = size - (toIndex-fromIndex);
	while (size != newSize)
	    elementData[--size] = null;
    }

    
    private void RangeCheck(int index) {
	if (index >= size)
	    throw new IndexOutOfBoundsException(
		"Index: "+index+", Size: "+size);
    }

    
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
	// Write out element count, and any hidden stuff
	int expectedModCount = modCount;
	s.defaultWriteObject();

        // Write out array length
        s.writeInt(elementData.length);

	// Write out all elements in the proper order.
	for (int i=0; i<size; i++)
            s.writeObject(elementData[i]);

	if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

    }

    
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
	// Read in size, and any hidden stuff
	s.defaultReadObject();

        // Read in array length and allocate array
        int arrayLength = s.readInt();
        Object[] a = elementData = new Object[arrayLength];

	// Read in all elements in the proper order.
	for (int i=0; i<size; i++)
            a[i] = s.readObject();
    }
}
