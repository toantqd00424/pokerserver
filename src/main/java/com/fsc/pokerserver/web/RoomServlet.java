package com.fsc.pokerserver.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fcs.pokerserver.BlindLevel;
import com.fcs.pokerserver.Player;
import com.fcs.pokerserver.Room;
import com.fcs.pokerserver.gameserver.MqttServletGameServer;
import com.google.common.base.Joiner;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import static com.googlecode.objectify.ObjectifyService.ofy;

@WebServlet(name = "RoomServlet", urlPatterns = { "/api/room" })

public class RoomServlet extends HttpServlet {

	MqttServletGameServer server = MqttServletGameServer.getInstance();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String method = request.getParameter("method");
		if ("put".equalsIgnoreCase(method)) {
			doPut(request, response);
			return;
		} else if ("join".equalsIgnoreCase(method)) {
			doPost(request, response);
			return;
		} else {
			//TODO return more data ex Blind level , number of player , game status ...
/*
 * List All room
 * 
 * */
			String data = Joiner.on(",").join(this.server.getListRoom());
			response.getWriter().println(data);

		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Player p = (Player) request.getAttribute("player");

		String id = request.getParameter("id");

		Room room = server.getRoomByID(Long.parseLong(id));

		checkNotNull(room, "Room " + id + " not found");

		room.addPlayer(p);
		
		//TODO return more data ex Blind level ,  player balance  , game status ...
		String data = Joiner.on(",").join(this.server.getListPlayer());

		response.getWriter().println(data);

	}

	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Player p = (Player) request.getAttribute("player");

		Room room = new Room(p, BlindLevel.BLIND_10_20);

		server.addRoom(room);

		response.getWriter().println(room.getRoomID());

	}
}