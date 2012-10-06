/**
 * @file
 */
package graph;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * No edge data because of various options on implementation.
 * So, members e and adj are missing.
 * @author Marshall Farrier
 * @date 11/23/10
 *
 */
public abstract class AbstractGraph implements Graph {
	private int v;
	// number of data fields
	private int d;
	private boolean directed;
	/**
	 * Vertices will always be integers internally,
	 * but for input and output, the class also supports
	 * using consecutive letters (a, b, c, s, t is NOT allowed!!).
	 * There must of course also be sufficiently many letters
	 * to allow labelling of all vertices
	 */
	private boolean charRep;
	// value of the starting letter
	private char offset;
	// for support of satellite data, empty by default
	private int[][] data;
	/**
	 * labels can be 'a' to 'z' only
	 */
	private int [] dataFieldLabels;
	protected int time;		// for DFS (CLRS, p. 604)
	final int ALPHABET_SIZE = 26;	// used for dataFieldLabels
	
	protected static final int VERTICES_SORTED = 0;
	protected static final int VERTEX_LOOKUP = 1;
	
	/**
	 * For fast internal processing.
	 * No use of offsets, and no input validation.
	 * @param _vert
	 * @param _dataType
	 * @param _val
	 */
	protected void setData(int _vert, int _dataType, int _val) {
		data[_dataType][_vert] = _val;
	}
	protected int getData(int _vert, int _dataType) {
		return data[_dataType][_vert];
	}
	
	/**
	 * Sorts vertices according to the field
	 * orderField, the values of which are assumed to be unique and between
	 * 0 and orderMax. Sorts in time O(v).
	 * The returned array will contain in the fields result[VERTICES_SORTED][i]
	 * an ordered list of vertices.
	 * The fields result[VERTEX_LOOKUP][u] will show the place of 
	 * vertex u in the list result[VERTICES_SORTED].
	 * @param orderField
	 * @param orderMax
	 * @param asc Sorts in ascending order if true, descending order if false.
	 * @return
	 */
	protected int[][] sortedVertices(int orderField, int orderMax, boolean asc) {
		final int ROWS = 2;
		int[][] result = new int[ROWS][v];
		int i, j;
		
		int[] tmpArr = new int[orderMax + 1];
		
		// Initialize tmpArr[i] to -1
		for (i = 0; i <= orderMax; ++i) {
			tmpArr[i] = -1;
		}
		// Update values according to vertex data
		for (i = 0; i < v; ++i) {
			tmpArr[data[orderField][i]] = i;
		}
		j = 0;
		if (asc) {
			for (i = 0; i <= orderMax; ++i) {
				if (tmpArr[i] >= 0) result[VERTICES_SORTED][j++] = tmpArr[i];
			}
		}
		else {
			for (i = orderMax; i >= 0; --i) {
				if (tmpArr[i] >= 0) result[VERTICES_SORTED][j++] = tmpArr[i];
			}
		}
		
		// Now create the lookup array
		for (i = 0; i < v; ++i) {
			result[VERTEX_LOOKUP][result[VERTICES_SORTED][i]] = i;
		}
		return result;
	}

// constructors
	/**
	 * Graphs are directed by default
	 */
	public AbstractGraph(int _v) {
		if (_v < 0) {
			throw new IllegalArgumentException("Number of vertices cannot be negative");
		}
		v = _v;
		d = 0;
		directed = true;
		charRep = false;
		offset = 0;
		time = 0;
		// data need not be initialized under default constructor
	}
	
	public AbstractGraph(int _v, boolean _directed) {
		this(_v);
		directed = _directed;
	}
	
	/**
	 * Character representation is allowed only with the alphabets 'A' - 'Z'
	 * and 'a' - 'z'. Lettering MUST also be consecutive!
	 * Entering an offset of 0 sets the graph to the (default) representation
	 * of vertices as integers. So, LinkedListGraph(_v, _directed, 0) is equivalent
	 * to LinkedListGraph(_v, _directed)
	 * @param _v
	 * @param _directed
	 * @param _offset
	 */
	public AbstractGraph(int _v, boolean _directed, int _offset) {		
		if (_v < 0) {
			throw new IllegalArgumentException("Number of vertices cannot be negative");
		}
		if (_v > 26) {
			throw new IllegalArgumentException("Character representation can accommodate no more than 26 vertices");
		}
		if (_offset < 'A' && _offset != 0) {
			throw new IllegalArgumentException("Invalid offset");
		}
		else if (_offset > 'z') {
			throw new IllegalArgumentException("Invalid offset");
		}
		else if (_offset <= 'Z' && _offset + _v - 1 > 'Z') {
			throw new IllegalArgumentException("Offset incompatible with number of vertices");
		}
		else if ('Z' < _offset && _offset < 'a') {
			throw new IllegalArgumentException("Invalid offset");
		}
		else if (_offset <= 'z' && _offset + _v - 1 > 'z') {
			throw new IllegalArgumentException("Offset incompatible with number of vertices");
		}
		v = _v;		
		d = 0;
		directed = _directed;
		if (_offset == 0) charRep = false;
		else charRep = true;
		offset = (char)_offset;
	}
	
	public AbstractGraph(int _v, boolean _directed, int _offset, int _dataFields) {
		if (_v < 0) {
			throw new IllegalArgumentException("Number of vertices cannot be negative");
		}
		if (_v > 26) {
			throw new IllegalArgumentException("Character representation can accommodate no more than 26 vertices");
		}
		if (_offset < 'A' && _offset != 0) {
			throw new IllegalArgumentException("Invalid character");
		}
		else if (_offset > 'z') {
			throw new IllegalArgumentException("Invalid character");
		}
		else if (_offset <= 'Z' && _offset + _v - 1 > 'Z') {
			throw new IllegalArgumentException("Offset incompatible with number of vertices");
		}
		else if ('Z' < _offset && _offset < 'a') {
			throw new IllegalArgumentException("Invalid character");
		}
		else if (_offset <= 'z' && _offset + _v - 1 > 'z') {
			throw new IllegalArgumentException("Character set incompatible with number of vertices");
		}
		else if (_dataFields < 0) {
			throw new IllegalArgumentException("Cannot have negative number of data fields");
		}
		
		v = _v;
		d = _dataFields;
		directed = _directed;
		if (_offset == 0) charRep = false;
		else charRep = true;
		offset = (char)_offset;
		if (_dataFields > 0) {
			data = new int[_dataFields][_v];
			dataFieldLabels = new int[ALPHABET_SIZE];
			for (int i = 0; i < ALPHABET_SIZE; ++i) {
				dataFieldLabels[i] = -1;	// sentinel value for unset
			}
		}		
	}
	
	public AbstractGraph(AbstractGraph _g) {
		this(_g.v, _g.directed, _g.offset, _g.d);
		int i, j;
		for (i = 0; i < d; ++i) {
			for (j = 0; j < v; ++j) {
				data[i][j] = _g.data[i][j];
			}
		}
		if (d > 0) {
			for (i = 0; i < ALPHABET_SIZE; ++i) {
				dataFieldLabels[i] = _g.dataFieldLabels[i];
			}
		}		
	}
	
	/**
	 * This constructor ignores the contents of the data fields
	 * in the graph to be copied and creates new data fields, all
	 * of which are initialized to 0. Contents of old data fields
	 * must be copied externally if they are needed in the copy.
	 * @param g
	 * @param d
	 */
	public AbstractGraph(AbstractGraph g, int dataFields) {
		this(g.v, g.directed, g.offset, dataFields);
	}
// basic accessors	
	public int vertices() { return v; }
	public boolean directed() { return directed; }
	public char offset() { return offset; }
	public boolean charRep() { return charRep; }
	public int dataFields() { return d; }

// methods for working with data fields
	/**
	 * allows labeling of data fields with a character 'a' to 'z'.
	 * This method will override any previous labeling of the same field.
	 * Example: g.setDataFieldLabel(1, 'c')
	 * @param _field
	 * @param _label
	 */
	public void setDataFieldLabel(int _field, char _label) {
		if (_field < 0 || d <= _field) {
			throw new IllegalArgumentException("Invalid data field");
		}
		if (_label < 'a' || 'z' < _label) {
			throw new IllegalArgumentException("Invalid label");
		}
		// resets prior labels
		for (int i = 0; i < ALPHABET_SIZE; ++i) {
			if (dataFieldLabels[i] == _field) dataFieldLabels[i] = -1;
		}
		dataFieldLabels[_label - 'a'] = _field;
	}
	

	/**
	 * 
	 * @param _vert Must be entered as a char (e.g., 'a') if graph uses character representation
	 * @param _dataType May be entered as a char label ('a' to 'z') if data type labels have been set
	 * @param _val
	 */
	public void setVertexData(int _vert, int _dataType, int _val) {
		int _v = _vert - offset;
		if (_v < 0 || v <= _v) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		int _d;
		if ('a' <= _dataType && _dataType <= 'z' && dataFieldLabels[_dataType] >= 0) {
			_d = dataFieldLabels[_dataType];
		}
		else _d = _dataType;
		if (_d < 0 || d <= _d) {
			throw new IllegalArgumentException("Invalid data type");
		}
		data[_d][_v] = _val;
	}	
	
	public int getVertexData(int _vert, int _dataType) {
		int _v = _vert - offset;
		if (_v < 0 || v <= _v) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		int _d;
		if ('a' <= _dataType && _dataType <= 'z' && dataFieldLabels[_dataType] >= 0) {
			_d = dataFieldLabels[_dataType];
		}
		else _d = _dataType;
		if (_d < 0 || d <= _d) {
			throw new IllegalArgumentException("Invalid data type");
		}
		return data[_d][_v];
	}
	
	public void showVertexData() {
		if (d == 0) {
			throw new NullPointerException("Graph contains no vertex data");
		}
		int i, j, k, tmp, maxDataWidth = 1, maxVertWidth = 1;
		// calculate formatting parameters
		boolean found;
		if (!charRep) maxVertWidth = String.valueOf(v - 1).length();
		for (i = 0; i < d; ++i) {
			if (String.valueOf(i).length() > maxDataWidth) {
				found = false;
				for (j = 0; j < ALPHABET_SIZE; ++j) {
					if (dataFieldLabels[j] == i) {
						found = true;
						break;
					}
				}
				if (!found) {
					maxDataWidth = String.valueOf(i).length();
				}
			}
			for (j = 0; j < v; ++j) {
				if (String.valueOf(data[i][j]).length() > maxDataWidth) {
					maxDataWidth = String.valueOf(data[i][j]).length();
				}
			}
		}
		
		// print column headings
		for (i = 0; i < maxVertWidth; ++i) {
			System.out.print(" ");
		}
		System.out.print(" ");		
		for (i = 0; i < d; ++i) {
			System.out.print("  ");
			found = false;
			for (j = 0; j < ALPHABET_SIZE; ++j) {
				if (dataFieldLabels[j] == i) {
					found = true;
					for (k = 0; k < maxDataWidth - 1; ++k) {
						System.out.print(" ");
					}
					System.out.print((char)(j + 'a'));
					break;
				}
			}
			if (!found) {
				tmp = maxDataWidth - String.valueOf(i).length();
				for (k = 0; k < tmp; ++k) {
					System.out.print(" ");
				}
				System.out.print(i);
			}
		}
		System.out.println();
		
		// print vertices and data
		for (i = 0; i < v; ++i) {
			if (charRep) {
				System.out.print((char)(i + offset) + ":");
			}
			else {
				tmp = maxVertWidth - String.valueOf(i).length();
				for (j = 0; j < tmp; ++j) {
					System.out.print(" ");
				}
				System.out.print(i + ":");
			}
			for (j = 0; j < d; ++j) {
				System.out.print("  ");
				tmp = maxDataWidth - String.valueOf(data[j][i]).length();
				for (k = 0; k < tmp; ++k) {
					System.out.print(" ");
				}
				System.out.print(data[j][i]);
			}
			System.out.println();
		}		
	}
	
	/**
	 * Shows data for a single field.
	 * The argument should be entered as a char (e.g., 'a') if
	 * the appropriate dataFieldLabel has been set.
	 * The data values will appear as integers in this
	 * version of the method.
	 * @param _dataField
	 */
	public void showVertexData(int _dataField) {
		int _d = _dataField, i;
		boolean charLabel = false;
		if ('a' <= _dataField && _dataField <= 'z') {
			for (i = 0; i < ALPHABET_SIZE; ++i) {
				if (dataFieldLabels[_dataField - 'a'] >= 0) {
					_d = dataFieldLabels[_dataField - 'a'];
					charLabel = true;
					break;
				}
			}
		}		
		if (_d < 0 || d <= _d) {
			throw new IllegalArgumentException("No such data field");
		}
		
		int j, tmp, maxDataWidth = 1, maxVertWidth = 1;
		// calculate formatting parameters
		if (!charRep) maxVertWidth = String.valueOf(v - 1).length();
		if (!charLabel) maxDataWidth = String.valueOf(_d).length();
		for (i = 0; i < v; ++i) {
			if (String.valueOf(data[_d][i]).length() > maxDataWidth) {
				maxDataWidth = String.valueOf(data[_d][i]).length();
			}
		}
		
		// print column heading
		for (i = 0; i < maxVertWidth; ++i) {
			System.out.print(" ");
		}
		System.out.print(" ");	// for ':'
		System.out.print("  ");
		if (charLabel) tmp = maxDataWidth - 1;
		else tmp = maxDataWidth - String.valueOf(_d).length();
		for (i = 0; i < tmp; ++i) {
			System.out.print(" ");
		}
		if (charLabel) System.out.println((char)_dataField);
		else System.out.println(_d);
		
		// print vertices and data
		for (i = 0; i < v; ++i) {
			if (charRep) {
				System.out.print((char)(i + offset) + ":");
			}
			else {
				tmp = maxVertWidth - String.valueOf(i).length();
				for (j = 0; j < tmp; ++j) {
					System.out.print(" ");
				}
				System.out.print(i + ":");
			}
			System.out.print("  ");
			tmp = maxDataWidth - String.valueOf(data[_d][i]).length();
			for (j = 0; j < tmp; ++j) {
				System.out.print(" ");
			}
			System.out.println(data[_d][i]);
		}
	}
	
	/**
	 * If _offset is 0, this method does the same thing as
	 * using showVertexData(_dataField).
	 * Otherwise, _offset must be in ['a'..'z'] or ['A'..'Z'],
	 * in which case the data field will be interpreted as char values
	 * if possible. If the data field does not map to the range ['a'..'z'] or
	 * ['A'..'Z'], the value will be output as int.
	 * @param _dataField
	 * @param _offset
	 */
	public void showVertexData(int _dataField, char _offset) {
	// validate _offset input
		if (_offset < 'A' && _offset != 0) {
			throw new IllegalArgumentException("Invalid offset");
		}
		else if (_offset > 'z') {
			throw new IllegalArgumentException("Invalid offset");
		}
		else if ('Z' < _offset && _offset < 'a') {
			throw new IllegalArgumentException("Invalid offset");
		}
	// case where _offset is 0
		if (_offset == 0) {
			showVertexData(_dataField);
			return;
		}
	// validate _dataField input and look up char label	
		int _d = _dataField, i;
		boolean charLabel = false, validChar = false;
		if ('a' <= _dataField && _dataField <= 'z') {
			for (i = 0; i < ALPHABET_SIZE; ++i) {
				if (dataFieldLabels[_dataField - 'a'] >= 0) {
					_d = dataFieldLabels[_dataField - 'a'];
					charLabel = true;
					break;
				}
			}
		}		
		if (_d < 0 || d <= _d) {
			throw new IllegalArgumentException("No such data field");
		}
		
		int j, tmp, maxDataWidth = 1, maxVertWidth = 1;
		// calculate formatting parameters
		if (!charRep) maxVertWidth = String.valueOf(v - 1).length();
		if (!charLabel) maxDataWidth = String.valueOf(_d).length();
		for (i = 0; i < v; ++i) {
			if ('A' <= data[_d][i] + _offset && data[_d][i] + _offset <= 'Z') continue;
			if ('a' <= data[_d][i] + _offset && data[_d][i] + _offset <= 'z') continue;
			if (String.valueOf(data[_d][i]).length() > maxDataWidth) {
				maxDataWidth = 2;
				break;
			}
		}
		
		// print column heading
		for (i = 0; i < maxVertWidth; ++i) {
			System.out.print(" ");
		}
		System.out.print("  ");	// for ": "
		if (charLabel) tmp = maxDataWidth - 1;
		else tmp = maxDataWidth - String.valueOf(_d).length();
		for (i = 0; i < tmp; ++i) {
			System.out.print(" ");
		}
		if (charLabel) System.out.println((char)_dataField);
		else System.out.println(_d);
		
		// print vertices and data
		for (i = 0; i < v; ++i) {
			if (charRep) {
				System.out.print((char)(i + offset) + ":");
			}
			else {
				tmp = maxVertWidth - String.valueOf(i).length();
				for (j = 0; j < tmp; ++j) {
					System.out.print(" ");
				}
				System.out.print(i + ":");
			}
			System.out.print(" ");
			if ('A' <= data[_d][i] + _offset && data[_d][i] + _offset <= 'Z') {
				tmp = maxDataWidth - 1;
				validChar = true;
			}
			else if ('a' <= data[_d][i] + _offset && data[_d][i] + _offset <= 'z') {
				tmp = maxDataWidth - 1;
				validChar = true;
			}
			else {
				tmp = maxDataWidth - String.valueOf(data[_d][i]).length();
				validChar = false;
			}
			
			for (j = 0; j < tmp; ++j) {
				System.out.print(" ");
			}
			if (validChar) {
				System.out.println((char)(data[_d][i] + _offset));
			}
			else System.out.println(-1);
			
		}
	}
	
	
}
