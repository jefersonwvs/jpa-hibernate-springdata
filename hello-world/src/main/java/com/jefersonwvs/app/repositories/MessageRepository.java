package com.jefersonwvs.app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jefersonwvs.app.entities.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {

}
