package com.sovos.dataregister.controller;

import com.sovos.dataregister.entity.Pessoa;
import com.sovos.dataregister.repository.PessoaRepository;
import org.hibernate.annotations.common.reflection.XMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(value = "/pessoa")  // clean code do requestMaapping

public class PessoaController {

    @Autowired
    private PessoaRepository _pessoaRepository;

    @RequestMapping(value = "/pessoa", method = RequestMethod.POST)
    public Pessoa save(@RequestBody Pessoa pessoa) {
        return _pessoaRepository.save(pessoa);

    }

    @RequestMapping(value = "/pessoa", method = RequestMethod.GET)
    public List<Pessoa> allPessoa() {
        return _pessoaRepository.findAll();
    }

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pessoa> getById(@PathVariable Integer id) {

        Optional<Pessoa> pessoa = _pessoaRepository.findById((long) id);

        if (pessoa.isPresent()) {
            return new ResponseEntity<>(pessoa.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Pessoa> update(@PathVariable Integer id, @RequestBody Pessoa pessoa) {

        Optional<Pessoa> oldPessoa = _pessoaRepository.findById((long) id);

        if (oldPessoa.isPresent()) {
            Pessoa p = new Pessoa();
            p.setPessoaId(id);
            p.setNome(pessoa.getNome());
            p.setEndereco(pessoa.getEndereco());
            p.setCpf(pessoa.getCpf());
            _pessoaRepository.save(p);
            return new ResponseEntity<>(p,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {

    Optional<Pessoa> pessoa = _pessoaRepository.findById(id);

    if(pessoa.isPresent()) {
        _pessoaRepository.delete(pessoa.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }else{
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    }


}
