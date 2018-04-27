package com.spbpu.hackaton;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;


@RestController
public class Controller {

//    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
//    public ResponseEntity<?> get(final @PathVariable String name,
//                                 final @RequestParam(value = "attributes", required = false)
//                                         Set<String> attributes) {
//
//        Settings settings = new Settings();
//        InputStream inputStream = db.getInputStream(name);
//        if (attributes != null) {
//            settings.deleteNodeName(attributes);
//        }
//
//        if (inputStream != null) {
//            tree = new XsdTreeObject(db.getInputStream(name));
//            return new ResponseEntity<>(tree.getTree(settings), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("XSD with name: " + name + " not found\n", HttpStatus.BAD_REQUEST);
//    }

    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    public ResponseEntity<?> get() {
        PrintRDD printRDD = new PrintRDD();
        return new ResponseEntity<>(printRDD.get(), HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<?> add(final @RequestParam("xsdScheme") MultipartFile uploadFile,
//                                 final @RequestParam("name") String name) {
//        return new ResponseEntity<>(content, responseHeaders, HttpStatus.OK);
//    }
}
