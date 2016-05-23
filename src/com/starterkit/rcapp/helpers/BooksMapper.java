package com.starterkit.rcapp.helpers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starterkit.rcapp.data.to.BookTo;

public class BooksMapper {
	
		static ObjectMapper objectMapper = new ObjectMapper();
	
	
		public static List<BookTo> mapJson2BookVoList(String jsonString) throws JsonParseException, JsonMappingException, IOException{
			return  objectMapper.readValue(jsonString, new TypeReference<List<BookTo>>(){});
		}

		public static BookTo mapJson2BookVo(String jsonString) throws JsonParseException, JsonMappingException, IOException{
			return  objectMapper.readValue(jsonString, BookTo.class);
		}
		
		public static String toJSON(BookTo bookTo) throws JsonProcessingException {
			return objectMapper.writeValueAsString(bookTo);
		}
	}

