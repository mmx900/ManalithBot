package org.manalith.ircbot.plugin.sample
import org.manalith.ircbot.plugin.AbstractBotPlugin
import org.manalith.ircbot.resources.MessageEvent
import org.manalith.ircbot.common.stereotype.BotCommand

class HelloScalaPlugin extends AbstractBotPlugin {
	override def getName() = {
		"Sample Scala Plugin";
	}

	override def getCommands() = {
		"!helloScala";
	}

	@BotCommand(Array("!helloScala"))
	def sayHello() = {
		"Hello Scala!"
	}
}