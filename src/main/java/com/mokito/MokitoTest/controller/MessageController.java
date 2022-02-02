package com.mokito.MokitoTest.controller;

import com.mokito.MokitoTest.handle.ApiRestException;
import com.mokito.MokitoTest.model.Message;
import com.mokito.MokitoTest.service.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coder")
public class MessageController {

    Logger logger = LogManager.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @GetMapping("/mensajes/example")
    public String getMensajesString() {
        logger.info("GET Request recibido string");
        return "Ejemplo de respuesta";
    }

    @GetMapping("/mensajes/all")
    public List<Message> getMensajes() {
        logger.info("GET Request recibido string");
        return dataMensajes();
    }

    @GetMapping("/mensajes")
    public List<Message> getMensajesByDescription(@RequestParam String description) {
        logger.info("GET obtener mensajes que sean iguales a la descripción");
        var msjFiltered = dataMensajes().stream()
                .filter(mensajes -> mensajes.getDescription().equalsIgnoreCase(description));
        return msjFiltered.collect(Collectors.toList());
    }

    @GetMapping("/mensajes/{id}")
    public Message getMensajeById(@PathVariable Long id) throws ApiRestException {
        logger.info("GET obtener mensaje por el id");

        if (id == 0) {
            throw new ApiRestException("El id del mensaje debe ser mayor a 0");
        }
        var msjFiltered = dataMensajes().stream()
                .filter(mensajes -> Objects.equals(mensajes.getId(), id));
        return msjFiltered.findFirst().orElse(new Message(0L, "No existe el mensaje"));
    }


    private List<Message> dataMensajes() {
        return List.of(
                new Message(1L, "Mensaje-ABCD"),
                new Message(2L, "Mensaje-ABCD"),
                new Message(3L, "Mensaje-ABCD"),
                new Message(4L, "Mensaje-ABCE"),
                new Message(5L, "Mensaje-ABCF")
        );
    }
}
