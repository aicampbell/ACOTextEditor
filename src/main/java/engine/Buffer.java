package engine;

import com.sun.org.apache.xml.internal.serializer.utils.StringToIntTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mo on 14.10.16.
 */
public class Buffer {
    private List<String> content;

    public Buffer() {
        content = new ArrayList<String>();
    }

    /**
     * inistializing the Buffer by a buf
     * @param buf default elements of buffer
     */
    public Buffer(Iterator<String> buf)
    {
        content = new ArrayList<String>();
        if(buf!=null)
        {
            while(buf.hasNext())
            {
                content.add(buf.next());
            }
        }
    }

    /**
     * insert items in specified index
     * @param character, is the character to be put in buffer
     * @param position, the position of inserting the character
     *                  position should be between 0 to buffer length
     */
    public void insert(String character, int position)
    {
        if(position<0 || position>content.size()) {
            throw new IndexOutOfBoundsException();
        }
        else {
            content.add(position, character);
        }
    }

    public void insert(Buffer buf, int position)
    {
        if(buf!=null)
        {
            Iterator<String> bi = buf.getIterator();
            while (bi.hasNext())
            {
                insert(bi.next(), position);
                position++;
            }
        }
    }

    /**
     * deletes the last character of buffer
     */
    public void delete()
    {
        if(content.size()>0)
        {
            content.remove(content.size()-1);
        }
    }

    /**
     * removes the items in content form index start to end parameters
     * @param start: start index for deleting
     * @param end: end index for deleting
     */
    public void delete(int start, int end)
    {
        if(start<0 || start>content.size())
        {
            throw new IndexOutOfBoundsException("start index is out of bound");
        }
        else if (end<0 || end>content.size())
        {
            throw new IndexOutOfBoundsException("end index is out of bound");
        }
        else if(start>end)
        {
            throw new IndexOutOfBoundsException("start index is bigger than end index");
        }
        else
        {
            content.subList(start, end).clear();
        }
    }

    public int getSize()
    {
        return content.size();
    }

    /**
     * copy a sub list of the buffer
     * @param start start index
     * @param end end index
     * @return reuturns an iterator of selected items
     */
    public Iterator<String> copy(int start, int end)
    {
        if(start<0 || start>content.size())
        {
            throw new IndexOutOfBoundsException("start index is out of bound");
        }
        else if (end<0 || end>content.size())
        {
            throw new IndexOutOfBoundsException("end index is out of bound");
        }
        else if(start>end)
        {
            throw new IndexOutOfBoundsException("start index is bigger than end index");
        }
        else
        {
            return content.subList(start, end).listIterator();
        }
    }

    public Iterator<String> getIterator()
    {
        return content.iterator();
    }

}
