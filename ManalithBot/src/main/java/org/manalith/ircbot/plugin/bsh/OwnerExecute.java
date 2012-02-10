/*
 * Created on 2005. 7. 28
 */
package org.manalith.ircbot.plugin.bsh;


import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;


public class OwnerExecute implements CommandExecutable, Runnable{
	private Logger logger = Logger.getLogger(getClass());
	private String command;
	private boolean isExecuted = false;
	private Object result = null;
	private Thread thisThread;
	private IRCController controller = null;
	private Interpreter interpreter = null;
	
	public OwnerExecute(){
		this.thisThread = new Thread(this);
	}
	
	public void exec(String command){
		this.command = command;
		thisThread.start();
	}
	
	public boolean isExecuted(){
		return isExecuted;
	}
	
	public Object getResult(){
		return result;
	}
	
	public void run() {
		Object result = null;
		try {
			if(interpreter == null){
				interpreter = new Interpreter();
				//interpreter.eval("import import org.manalith.ircbot.IRCController");
				//interpreter.set("irc", controller);
			}
			
			result = interpreter.eval(command);
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
