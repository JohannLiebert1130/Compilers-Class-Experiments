/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
    int comment_depth=0;
    Boolean strTooLong=false;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 3;
	private final int LINE_COMMENT = 2;
	private final int BLOCK_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		49,
		54,
		86
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NO_ANCHOR,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NOT_ACCEPT,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"9,7:7,11,13,6,11,13,5,7:13,12,7:4,13,7,8,7:5,2,4,3,40,38,1,41,39,64:10,42,3" +
"6,37,34,35,7,45,20,63,23,31,18,24,63,27,25,63:2,21,63,26,30,32,63,16,22,28," +
"17,29,33,63:3,7,10,7:2,62,7,47,48,49,50,51,19,48,52,53,48:2,54,48,55,56,57," +
"48,58,59,15,60,14,61,48:3,43,7,44,46,7,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,179,
"0,1,2,3,4,1,5,1:3,6,7,8,1,9,1:12,10,7,11,7,1:3,7:3,10:2,7:5,10,7:4,12,1:4,1" +
"3,1:9,14,1,10,15,16,7,10,17,10:4,7,10:9,18,19,20,21:2,22,23,21,24,25,26,27," +
"28,26,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51," +
"52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76," +
"77,78,79,80,81,82,83,84,85,86,87,88,89,90,10,91,92,93,94,95,96,97,98,99,100" +
",101,102,103,104,105,106")[0];

	private int yy_nxt[][] = unpackFromString(107,65,
"1,2,3,4,5,6,65,7,8,7:4,9,66,10,11:2,163,67,11,129,11,165,68,92,131,11,167,1" +
"1,98,11,169,175,12,7,13,14,15,16,17,18,19,20,21,22,23,162:2,164,162,166,162" +
",91,128,130,97,168,162:3,170,7,11,64,-1:66,24,-1:66,25,-1:65,26,-1:66,65,-1" +
":72,162:2,132,162:10,134,162:6,-1:13,162:5,134,162:5,132,162:6,-1:14,11:20," +
"-1:13,11:18,-1:35,31,-1:30,32,-1:32,33,-1:44,162:20,-1:13,162:18,-1:14,11:1" +
"3,178,11:6,-1:13,11:5,178,11:12,1,50,87,94,50,-1,51,50:58,1,88:5,55,88:58,-" +
"1:64,64,-1:14,162:6,136,162:4,27,162:8,-1:13,136,162:5,27,162:11,-1:14,11:1" +
"1,69,11:8,-1:13,11:6,69,11:11,-1:14,162:13,156,162:6,-1:13,162:5,156,162:12" +
",1,56:4,57,58,56,59,89,95,56,60,56:52,-1:3,52,-1:62,88:5,-1,88:58,-1,93:7,6" +
"1,93:56,-1:14,162:5,70,162:2,146,162,70,162,71,162:7,-1:13,162:8,71,162:3,1" +
"46,162:5,-1:14,11:5,28,11:2,176,11,28,11,29,11:7,-1:13,11:8,29,11:3,176,11:" +
"5,-1:4,53,-1:61,62:4,99,63,62:2,90,62,96,62,96,62:51,-1:5,99,63,-1:4,99,-1," +
"99,-1:65,162:5,72,162:4,72,162:9,-1:13,162:18,-1:14,11:5,30,11:4,30,11:9,-1" +
":13,11:18,-1:14,162,73,162:12,73,162:5,-1:13,162:18,-1:14,11,34,11:12,34,11" +
":5,-1:13,11:18,-1:14,162:19,74,-1:13,162:14,74,162:3,-1:14,11:19,35,-1:13,1" +
"1:14,35,11:3,-1:14,162,75,162:12,75,162:5,-1:13,162:18,-1:14,11,36,11:12,36" +
",11:5,-1:13,11:18,-1:14,162:4,37,162:15,-1:13,162:4,37,162:13,-1:14,11:4,39" +
",11:15,-1:13,11:4,39,11:13,-1:14,162:12,38,162:7,-1:13,162:8,38,162:9,-1:14" +
",11:9,40,11:10,-1:13,11:2,40,11:15,-1:14,162:4,80,162:15,-1:13,162:4,80,162" +
":13,-1:14,11:18,41,11,-1:13,11:10,41,11:7,-1:14,162:4,77,162:15,-1:13,162:4" +
",77,162:13,-1:14,11:4,42,11:15,-1:13,11:4,42,11:13,-1:14,162:9,78,162:10,-1" +
":13,162:2,78,162:15,-1:14,11:12,76,11:7,-1:13,11:8,76,11:9,-1:14,162:18,79," +
"162,-1:13,162:10,79,162:7,-1:14,11:7,43,11:12,-1:13,11:7,43,11:10,-1:14,162" +
":7,81,162:12,-1:13,162:7,81,162:10,-1:14,11:8,45,11:11,-1:13,11:12,45,11:5," +
"-1:14,162:4,44,162:15,-1:13,162:4,44,162:13,-1:14,11:4,46,11:15,-1:13,11:4," +
"46,11:13,-1:14,162:8,82,162:11,-1:13,162:12,82,162:5,-1:14,11:17,47,11:2,-1" +
":13,11:3,47,11:14,-1:14,162:4,83,162:15,-1:13,162:4,83,162:13,-1:14,11:8,48" +
",11:11,-1:13,11:12,48,11:5,-1:14,162:17,84,162:2,-1:13,162:3,84,162:14,-1:1" +
"4,162:8,85,162:11,-1:13,162:12,85,162:5,-1:14,162:4,100,162:11,148,162:3,-1" +
":13,162:4,100,162:4,148,162:8,-1:14,11:4,101,11:11,137,11:3,-1:13,11:4,101," +
"11:4,137,11:8,-1:14,162:4,102,162:11,104,162:3,-1:13,162:4,102,162:4,104,16" +
"2:8,-1:14,11:4,103,11:11,105,11:3,-1:13,11:4,103,11:4,105,11:8,-1:14,162:3," +
"106,162:16,-1:13,162:13,106,162:4,-1:14,11:8,107,11:11,-1:13,11:12,107,11:5" +
",-1:14,162:4,108,162:15,-1:13,162:4,108,162:13,-1:14,11:6,109,11:13,-1:13,1" +
"09,11:17,-1:14,162:7,153,162:12,-1:13,162:7,153,162:10,-1:14,11:16,111,11:3" +
",-1:13,11:9,111,11:8,-1:14,162:8,110,162:11,-1:13,162:12,110,162:5,-1:14,11" +
":8,113,11:11,-1:13,11:12,113,11:5,-1:14,162:6,154,162:13,-1:13,154,162:17,-" +
"1:14,11:4,115,11:15,-1:13,11:4,115,11:13,-1:14,162:8,112,162:11,-1:13,162:1" +
"2,112,162:5,-1:14,11:16,117,11:3,-1:13,11:9,117,11:8,-1:14,162:6,114,162:13" +
",-1:13,114,162:17,-1:14,11:8,119,11:11,-1:13,11:12,119,11:5,-1:14,155,162:1" +
"4,155,162:4,-1:13,162:18,-1:14,11:7,121,11:12,-1:13,11:7,121,11:10,-1:14,16" +
"2:16,116,162:3,-1:13,162:9,116,162:8,-1:14,11:11,123,11:8,-1:13,11:6,123,11" +
":11,-1:14,162:16,118,162:3,-1:13,162:9,118,162:8,-1:14,11,125,11:12,125,11:" +
"5,-1:13,11:18,-1:14,162:11,157,162:8,-1:13,162:6,157,162:11,-1:14,162:8,120" +
",162:11,-1:13,162:12,120,162:5,-1:14,162:8,122,162:11,-1:13,162:12,122,162:" +
"5,-1:14,162:16,158,162:3,-1:13,162:9,158,162:8,-1:14,162:4,159,162:15,-1:13" +
",162:4,159,162:13,-1:14,162:7,124,162:12,-1:13,162:7,124,162:10,-1:14,162:1" +
"1,126,162:8,-1:13,162:6,126,162:11,-1:14,162:2,160,162:17,-1:13,162:11,160," +
"162:6,-1:14,162:11,161,162:8,-1:13,162:6,161,162:11,-1:14,162,127,162:12,12" +
"7,162:5,-1:13,162:18,-1:14,11:7,133,135,11:11,-1:13,11:7,133,11:4,135,11:5," +
"-1:14,162:6,138,140,162:12,-1:13,138,162:6,140,162:10,-1:14,11:6,139,171,11" +
":12,-1:13,139,11:6,171,11:10,-1:14,162:7,142,144,162:11,-1:13,162:7,142,162" +
":4,144,162:5,-1:14,11:13,141,11:6,-1:13,11:5,141,11:12,-1:14,162:16,150,162" +
":3,-1:13,162:9,150,162:8,-1:14,11:16,143,11:3,-1:13,11:9,143,11:8,-1:14,162" +
":13,152,162:6,-1:13,162:5,152,162:12,-1:14,11:6,145,11:13,-1:13,145,11:17,-" +
"1:14,11:11,147,11:8,-1:13,11:6,147,11:11,-1:14,11:16,149,11:3,-1:13,11:9,14" +
"9,11:8,-1:14,11:11,151,11:8,-1:13,11:6,151,11:11,-1:14,11:13,172,11:6,-1:13" +
",11:5,172,11:12,-1:14,173,11:14,173,11:4,-1:13,11:18,-1:14,11:2,174,11:17,-" +
"1:13,11:11,174,11:6,-1:14,11:4,177,11:15,-1:13,11:4,177,11:13");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
    case BLOCK_COMMENT:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF encontered in block comment.");
    case STRING:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF encontered in string.");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 0:
						{
                                    return new Symbol(TokenConstants.INT_CONST,
                                                    AbstractTable.inttable.addString(yytext())); }
					case -2:
						break;
					case 1:
						
					case -3:
						break;
					case 2:
						{ return new Symbol(TokenConstants.MINUS);  }
					case -4:
						break;
					case 3:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -5:
						break;
					case 4:
						{ return new Symbol(TokenConstants.MULT);   }
					case -6:
						break;
					case 5:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -7:
						break;
					case 6:
						{ curr_lineno++; }
					case -8:
						break;
					case 7:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  //makSystem.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
                                  return new Symbol(TokenConstants.ERROR, yytext()); }
					case -9:
						break;
					case 8:
						{ string_buf.setLength(0); yybegin(STRING); }
					case -10:
						break;
					case 9:
						{ /* ignore */ }
					case -11:
						break;
					case 10:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -12:
						break;
					case 11:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -13:
						break;
					case 12:
						{ return new Symbol(TokenConstants.EQ);     }
					case -14:
						break;
					case 13:
						{ return new Symbol(TokenConstants.SEMI);   }
					case -15:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LT);     }
					case -16:
						break;
					case 15:
						{ return new Symbol(TokenConstants.COMMA);  }
					case -17:
						break;
					case 16:
						{ return new Symbol(TokenConstants.DIV);    }
					case -18:
						break;
					case 17:
						{ return new Symbol(TokenConstants.PLUS);   }
					case -19:
						break;
					case 18:
						{ return new Symbol(TokenConstants.DOT);    }
					case -20:
						break;
					case 19:
						{ return new Symbol(TokenConstants.COLON);  }
					case -21:
						break;
					case 20:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -22:
						break;
					case 21:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -23:
						break;
					case 22:
						{ return new Symbol(TokenConstants.AT);     }
					case -24:
						break;
					case 23:
						{ return new Symbol(TokenConstants.NEG);    }
					case -25:
						break;
					case 24:
						{ yybegin(LINE_COMMENT); }
					case -26:
						break;
					case 25:
						{ comment_depth++; yybegin(BLOCK_COMMENT); }
					case -27:
						break;
					case 26:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -28:
						break;
					case 27:
						{ return new Symbol(TokenConstants.FI);     }
					case -29:
						break;
					case 28:
						{ return new Symbol(TokenConstants.IF);     }
					case -30:
						break;
					case 29:
						{ return new Symbol(TokenConstants.IN);     }
					case -31:
						break;
					case 30:
						{ return new Symbol(TokenConstants.OF);     }
					case -32:
						break;
					case 31:
						{ return new Symbol(TokenConstants.DARROW); }
					case -33:
						break;
					case 32:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -34:
						break;
					case 33:
						{ return new Symbol(TokenConstants.LE);     }
					case -35:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LET);    }
					case -36:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NEW);    }
					case -37:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NOT);    }
					case -38:
						break;
					case 37:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));  }
					case -39:
						break;
					case 38:
						{ return new Symbol(TokenConstants.THEN);   }
					case -40:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ELSE);   }
					case -41:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ESAC);   }
					case -42:
						break;
					case 41:
						{ return new Symbol(TokenConstants.LOOP);   }
					case -43:
						break;
					case 42:
						{ return new Symbol(TokenConstants.CASE);   }
					case -44:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL);   }
					case -45:
						break;
					case 44:
						{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
					case -46:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS);  }
					case -47:
						break;
					case 46:
						{ return new Symbol(TokenConstants.WHILE);  }
					case -48:
						break;
					case 47:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 48:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{ /* ignore. Not doing .* because maximal munch will mess things up so one at a time instead */ }
					case -51:
						break;
					case 51:
						{ curr_lineno++; }
					case -52:
						break;
					case 52:
						{ comment_depth++;   /*handle potential nesting by keeping count*/ }
					case -53:
						break;
					case 53:
						{ 
		                        comment_depth--;
		                        if(comment_depth < 0) {
		                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
		                        }else if (comment_depth == 0) {
		                            /*the nesting was balanced and we are outside block comment*/
		                            yybegin(YYINITIAL);
		                        }else {
		                            /* ignore */
                        	    }
                    }
					case -54:
						break;
					case 54:
						{ /* ignore */ }
					case -55:
						break;
					case 55:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -56:
						break;
					case 56:
						{   
                     string_buf = string_buf.append(yytext().charAt(0));
                }
					case -57:
						break;
					case 57:
						{
                string_buf = string_buf.append('\015'); 
		}
					case -58:
						break;
					case 58:
						{   
                                        // unescaped new line
                                        yybegin(YYINITIAL);
                                        curr_lineno++; 
                                        string_buf.setLength(0);
                                        return new Symbol(TokenConstants.ERROR, "Unterminated string constant.");
                                    }
					case -59:
						break;
					case 59:
						{
                            yybegin(YYINITIAL);
                            if(string_buf.length() > MAX_STR_CONST) {
                              return new Symbol(TokenConstants.ERROR, "String constant too long");
                           }else {
                              String s = string_buf.toString();
                              string_buf.setLength(0);
                              return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(s));
                            }
                          }
					case -60:
						break;
					case 60:
						{
                 string_buf = string_buf.append('\033'); 
		}
					case -61:
						break;
					case 61:
						{ //eat up all characters after null and stop at ending quote (eat it too)
                            yybegin(YYINITIAL);
                            string_buf = new StringBuffer();
                            return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
                          }
					case -62:
						break;
					case 62:
						{ 
                    if (yytext().equals("\\n")) {
                        string_buf = string_buf.append('\n'); 
                    } else if (yytext().equals("\\b")){
                        string_buf = string_buf.append('\b'); 
                    } else if (yytext().equals("\\t")){
                        string_buf = string_buf.append('\t'); 
                    } else if (yytext().equals("\\f")){
                        string_buf = string_buf.append('\f'); 
                    }else {
                        string_buf = string_buf.append(yytext().charAt(1));
                    }
                }
					case -63:
						break;
					case 63:
						{   
                                        // escaped slash
                                        curr_lineno++;    
                                        string_buf = string_buf.append('\n');
                                    }
					case -64:
						break;
					case 64:
						{
                                    return new Symbol(TokenConstants.INT_CONST,
                                                    AbstractTable.inttable.addString(yytext())); }
					case -65:
						break;
					case 65:
						{ curr_lineno++; }
					case -66:
						break;
					case 66:
						{ /* ignore */ }
					case -67:
						break;
					case 67:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -68:
						break;
					case 68:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -69:
						break;
					case 69:
						{ return new Symbol(TokenConstants.FI);     }
					case -70:
						break;
					case 70:
						{ return new Symbol(TokenConstants.IF);     }
					case -71:
						break;
					case 71:
						{ return new Symbol(TokenConstants.IN);     }
					case -72:
						break;
					case 72:
						{ return new Symbol(TokenConstants.OF);     }
					case -73:
						break;
					case 73:
						{ return new Symbol(TokenConstants.LET);    }
					case -74:
						break;
					case 74:
						{ return new Symbol(TokenConstants.NEW);    }
					case -75:
						break;
					case 75:
						{ return new Symbol(TokenConstants.NOT);    }
					case -76:
						break;
					case 76:
						{ return new Symbol(TokenConstants.THEN);   }
					case -77:
						break;
					case 77:
						{ return new Symbol(TokenConstants.ELSE);   }
					case -78:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ESAC);   }
					case -79:
						break;
					case 79:
						{ return new Symbol(TokenConstants.LOOP);   }
					case -80:
						break;
					case 80:
						{ return new Symbol(TokenConstants.CASE);   }
					case -81:
						break;
					case 81:
						{ return new Symbol(TokenConstants.POOL);   }
					case -82:
						break;
					case 82:
						{ return new Symbol(TokenConstants.CLASS);  }
					case -83:
						break;
					case 83:
						{ return new Symbol(TokenConstants.WHILE);  }
					case -84:
						break;
					case 84:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -85:
						break;
					case 85:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -86:
						break;
					case 87:
						{ /* ignore. Not doing .* because maximal munch will mess things up so one at a time instead */ }
					case -87:
						break;
					case 88:
						{ /* ignore */ }
					case -88:
						break;
					case 89:
						{   
                     string_buf = string_buf.append(yytext().charAt(0));
                }
					case -89:
						break;
					case 90:
						{ 
                    if (yytext().equals("\\n")) {
                        string_buf = string_buf.append('\n'); 
                    } else if (yytext().equals("\\b")){
                        string_buf = string_buf.append('\b'); 
                    } else if (yytext().equals("\\t")){
                        string_buf = string_buf.append('\t'); 
                    } else if (yytext().equals("\\f")){
                        string_buf = string_buf.append('\f'); 
                    }else {
                        string_buf = string_buf.append(yytext().charAt(1));
                    }
                }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -91:
						break;
					case 92:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -92:
						break;
					case 94:
						{ /* ignore. Not doing .* because maximal munch will mess things up so one at a time instead */ }
					case -93:
						break;
					case 95:
						{   
                     string_buf = string_buf.append(yytext().charAt(0));
                }
					case -94:
						break;
					case 96:
						{ 
                    if (yytext().equals("\\n")) {
                        string_buf = string_buf.append('\n'); 
                    } else if (yytext().equals("\\b")){
                        string_buf = string_buf.append('\b'); 
                    } else if (yytext().equals("\\t")){
                        string_buf = string_buf.append('\t'); 
                    } else if (yytext().equals("\\f")){
                        string_buf = string_buf.append('\f'); 
                    }else {
                        string_buf = string_buf.append(yytext().charAt(1));
                    }
                }
					case -95:
						break;
					case 97:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -96:
						break;
					case 98:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -97:
						break;
					case 100:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -98:
						break;
					case 101:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -99:
						break;
					case 102:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -100:
						break;
					case 103:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -101:
						break;
					case 104:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -102:
						break;
					case 105:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -103:
						break;
					case 106:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -104:
						break;
					case 107:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -105:
						break;
					case 108:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -106:
						break;
					case 109:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -107:
						break;
					case 110:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -108:
						break;
					case 111:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -109:
						break;
					case 112:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -110:
						break;
					case 113:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -111:
						break;
					case 114:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -112:
						break;
					case 115:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -113:
						break;
					case 116:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -114:
						break;
					case 117:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -115:
						break;
					case 118:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -116:
						break;
					case 119:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -117:
						break;
					case 120:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -118:
						break;
					case 121:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -119:
						break;
					case 122:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -120:
						break;
					case 123:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -121:
						break;
					case 124:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -122:
						break;
					case 125:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -123:
						break;
					case 126:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -124:
						break;
					case 127:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -125:
						break;
					case 128:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -126:
						break;
					case 129:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -127:
						break;
					case 130:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -128:
						break;
					case 131:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -129:
						break;
					case 132:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -130:
						break;
					case 133:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -131:
						break;
					case 134:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -132:
						break;
					case 135:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -133:
						break;
					case 136:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -134:
						break;
					case 137:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -135:
						break;
					case 138:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -136:
						break;
					case 139:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -137:
						break;
					case 140:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -138:
						break;
					case 141:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -139:
						break;
					case 142:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -140:
						break;
					case 143:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -141:
						break;
					case 144:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -142:
						break;
					case 145:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -143:
						break;
					case 146:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -144:
						break;
					case 147:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -145:
						break;
					case 148:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -146:
						break;
					case 149:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -147:
						break;
					case 150:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -148:
						break;
					case 151:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -149:
						break;
					case 152:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -150:
						break;
					case 153:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -151:
						break;
					case 154:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -152:
						break;
					case 155:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -153:
						break;
					case 156:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -154:
						break;
					case 157:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -155:
						break;
					case 158:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -156:
						break;
					case 159:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -157:
						break;
					case 160:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -158:
						break;
					case 161:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -159:
						break;
					case 162:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -160:
						break;
					case 163:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -161:
						break;
					case 164:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -162:
						break;
					case 165:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -163:
						break;
					case 166:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -164:
						break;
					case 167:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -165:
						break;
					case 168:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -166:
						break;
					case 169:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -167:
						break;
					case 170:
						{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
					case -168:
						break;
					case 171:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -169:
						break;
					case 172:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -170:
						break;
					case 173:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -171:
						break;
					case 174:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -172:
						break;
					case 175:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -173:
						break;
					case 176:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -174:
						break;
					case 177:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -175:
						break;
					case 178:
						{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
					case -176:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
