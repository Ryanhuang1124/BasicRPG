package com.rpg;

public class king extends Human{
	
	//王様クラス
	
	public king() {
		super("王様", 100, 0);
	}
	
	//勇者に使命を与える
	
	public void giveMission() {
		talk("勇者よぉ…娘が…娘がぁぁぁ！");
		talk("魔王を倒して、わが娘を救ってくれぇぇぇ！");
	}
}