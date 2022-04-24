package com.sovos.dataveiculos.controller;

import com.sovos.dataveiculos.entity.Veiculos;
import com.sovos.dataveiculos.repository.VeiculosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
@RequestMapping(value = "/veiculos")
@RestController

public class VeiculosController {

    @Autowired
    private VeiculosRepository _veiculosRepository;


    @RequestMapping(method = RequestMethod.POST)
    public Veiculos save(@RequestBody Veiculos veiculos) {
        return _veiculosRepository.save(veiculos);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Veiculos> listarTodos() {
        return _veiculosRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Veiculos> apenasUmVeiculo(@PathVariable Integer id) {

        Optional<Veiculos> veiculos = _veiculosRepository.findById(id);

        if (veiculos.isPresent()) {
            return new ResponseEntity<>(veiculos.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Veiculos(), HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Veiculos> atualizar(@PathVariable Integer id, @RequestBody Veiculos veiculos) {

        Optional<Veiculos> oldveiculos = _veiculosRepository.findById(id);

        if (oldveiculos.isPresent()) {
            Veiculos v = oldveiculos.get();
            v.setPlaca(veiculos.getPlaca());
            v.setCpfProprietario(veiculos.getCpfProprietario());
            v.setCor(veiculos.getCor());
            _veiculosRepository.save(v);
            return new ResponseEntity<>(v, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deletar(@PathVariable Integer id) {
        Optional<Veiculos> veiculos = _veiculosRepository.findById(id);

        if (veiculos.isPresent()) {
            _veiculosRepository.delete(veiculos.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}




