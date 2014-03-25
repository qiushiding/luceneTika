package TestCode;

import java.io.IOException;

import lucene.IndexSearchProcess;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

public class SearchTest {

	public static void main(String[] args) throws CorruptIndexException,
			IOException, ParseException {
		IndexSearchProcess st = new IndexSearchProcess(
				"C:\\Users\\qiusd\\Desktop\\index");
		// st.search("doc-number", "5953778");
		st.searchInAllFields("5953778");
	}
}
