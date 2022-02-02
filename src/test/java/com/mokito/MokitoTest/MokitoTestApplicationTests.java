package com.mokito.MokitoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokito.MokitoTest.model.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
	@AutoConfigureMockMvc
	public class MokitoTestApplicationTests {

		private final ObjectMapper mapper = new ObjectMapper();

		@Autowired
		private MockMvc mockMvc;

		@BeforeAll
		static void setup() {
			System.out.println("@BeforeAll - Runs before All tests");
		}

		@BeforeEach
		void init() {
			System.out.println("@BeforeEach - Runs before each Tests");
		}

		@Test
		public void getAllMessages() throws Exception {
			var result = mockMvc.perform(get("/coder/mensajes/all"))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();

			var content = result.getResponse().getContentAsString();
			var messages = mapper.readValue(content, List.class);

			Assert.notNull(messages, "List not Null");
			Assert.notEmpty(messages, "List not Empty");
			Assert.isTrue(messages.size() == 4, "List size = 4");

		}

		@Test
		public void getMessageById() throws Exception {
			var result = mockMvc.perform(get("/coder/mensajes/{id}", 1))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();

			var content = result.getResponse().getContentAsString();
			var message = mapper.readValue(content, Message.class);

			Assert.notNull(message, "Message not null");
			Assert.isTrue(message.getId() == 1, "Message ID OKEY");
			Assert.isTrue(message.getDescription().equals("Mensaje-ABCD"), "Message description OKEY");
		}

		@Test
		public void testGetByDescription() throws Exception {
			var result = mockMvc.perform(get("/coder/mensajes?description=Mensaje-ABCD"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("Mensaje-ABCD")))
					.andReturn();

			String content = result.getResponse().getContentAsString();
			List<Message> messageValues = mapper.readValue(content, List.class);
			Assert.notNull(messageValues, "List not Null");
			Assert.notEmpty(messageValues, "List not empty");
			Assert.isTrue(messageValues.size() < 3, "ERROR-> List size should be 3");
		}
	}
