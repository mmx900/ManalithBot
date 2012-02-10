package org.manalith.ircbot.plugin.bsh;


import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

/*
 * Created on 2005. 7. 26
 */

public class RemoteExecute implements CommandExecutable, Runnable{
	private Logger logger = Logger.getLogger(getClass());
	private final String command;
	private boolean isExecuted = false;
	private Object result = null;
	private final Thread thisThread;
	private final boolean isOwner;
	private final IRCController controller;

	public RemoteExecute(String command){
		this.command = command;
		this.thisThread = new Thread(this);
		thisThread.start();
		this.isOwner = false;
		this.controller = null;
	}
	
	public RemoteExecute(String command, IRCController controller){
		this.command = command;
		this.thisThread = new Thread(this);
		thisThread.start();
		this.isOwner = true;
		this.controller = controller;
	}
	
	public boolean isExecuted(){
		return isExecuted;
	}
	
	public Object getResult(){
		return result;
	}
	
	public void run() {
		Object result = null;
		Interpreter i = new Interpreter();
		try {
			//초기화
			//0 : oper 전용 메서드 사용 가부
			if(isOwner){
				i.eval("import org.manalith.ircbot.IRCController");
			}
			//1 : System.exit(-1) 등을 비롯한 System 객체의 사용 방지
			i.eval("System = \"MERONG ERROR\"");
			result = i.eval(command);		
		}catch(TargetError ex){
			try{
				Exception e = (Exception) ex.getTarget();
				throw e;
			}catch(Exception e){
				result = e.toString();
				logger.error(e);
			}
		}catch(EvalError ex){
			result = ex.getMessage();
			logger.error(ex);
		}finally{
			this.result = result;
			isExecuted = true;
			i = null;
		}
	}
	
	public void stop(){
		thisThread.stop();
		/*
		thisThread.interrupt();
		try {
			thisThread.join(1000);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		*/
	}
	
}
