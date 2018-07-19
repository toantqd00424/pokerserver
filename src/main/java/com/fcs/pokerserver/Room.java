/*
The MIT License (MIT)
Copyright (c) 2018 Ngocbd
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.fcs.pokerserver;

import java.util.ArrayList;
import java.util.List;

import com.fcs.pokerserver.events.GameEvent;
import com.fcs.pokerserver.events.GameListener;

public class Room implements GameListener {
	Game currentGame =null;
	long RoomID;
	Player master = null;
	GameServer server = null;
	private List<Player> listPlayer = new ArrayList<Player>();
	
	public void addPlayer(Player p)
	{
		this.listPlayer.add(p);
		p.setCurrentRoom(this);
		
		if(this.currentGame.getStatus()==GameStatus.NOT_STARTED && this.currentGame.getListPlayer().size()<8)
		{
			
			this.currentGame.addPlayer(p);
		}
		
		String content = "cmd=playerJoin&id="+p.getName();
		this.server.sender.add(GameServer.SERVER_TOPIC+"/room/"+this.getRoomID(), content);
		
	}
	public List<Player> getListPlayer() {
		return listPlayer;
	}
	private void setListPlayer(List<Player> listPlayer) {
		this.listPlayer = listPlayer;
	}
	
	public Game getCurrentGame() {
		return currentGame;
	}
	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
	public long getRoomID() {
		return RoomID;
	}
	public void setRoomID(long roomID) {
		RoomID = roomID;
	}
	public Player getMaster() {
		return master;
	}
	
	public void setMaster(Player master) {
		this.master = master;
	}
	public BlindLevel getBlindLevel() {
		return blindLevel;
	}
	public void setBlindLevel(BlindLevel blindLevel) {
		this.blindLevel = blindLevel;
	}
	BlindLevel blindLevel; 
	public Room(Player master,BlindLevel blindLevel)
	{
		this.master=master;
		this.blindLevel=blindLevel; ;
		this.RoomID=System.currentTimeMillis();
		this.server=GameServer.getInstance();
		
		
		String content = "cmd=playerJoin&id="+master.getName();
		this.server.sender.add(GameServer.SERVER_TOPIC+"/room/"+this.getRoomID(), content);
		this.createNewGame();
		
	}
	public Game createNewGame()
	{
		this.currentGame = new Game(this);
		this.currentGame.addGameListener(this);
		this.currentGame.addPlayer(this.master);
		
		String content = "cmd=gameCreated&id="+this.currentGame.getId();
		this.server.sender.add(GameServer.SERVER_TOPIC+"/room/"+this.getRoomID(), content);
		return this.currentGame;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(this.RoomID);
	}
	@Override
	public void actionPerformed(GameEvent event) {
		String content = "cmd="+event.getAction()+"&id="+this.currentGame.getId();
		this.server.sender.add(GameServer.SERVER_TOPIC+"/room/"+this.getRoomID(), content);
		
	}
	
	
}
