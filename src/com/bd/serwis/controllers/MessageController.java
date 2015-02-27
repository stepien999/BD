package com.bd.serwis.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.bd.serwis.model.Message;
import com.bd.serwis.model.User;
import com.bd.serwis.services.MessageService;

@ManagedBean(name = "messageController")
@SessionScoped
public class MessageController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MessageService messageService = new MessageService();
	private Message message;
	private User reciepent;
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public User getReciepent() {
		return reciepent;
	}

	public void setReciepent(User reciepent) {
		this.reciepent = reciepent;
	}

	public String createMessage(User reciepent){
		this.reciepent = reciepent;
		message = new Message();
		return "redirect_send_message";
	}
	
	public String sendMessage(User user){
		message.setReciepent(reciepent);
		message.setSender(user);
		message.setDateOfSending(new Date());
		messageService.createMessage(message);
		return "send_message_success";
	}
	
	public List<Message> getAllSentMessages(User user){
        return messageService.findAllSentMessages(user.getUserId());
	}
	
	public List<Message> getAllReceivedMessages(User user){
        return messageService.findAllReceivedMessages(user.getUserId());
	}
	
	public String redirectToMessages(){
		return "redirect_to_messages";
	}
	
	public String redirectToSentMessages(){
		return "redirect_to_sent_messages";
	}
}
