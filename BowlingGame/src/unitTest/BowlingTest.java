package unitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BowlingTest {
	Game bg;
	private void rollMany(int n, int pins) {  // rollMany(丟了多少次,每顆球打到多少瓶子)
		for(int i = 0; i < n; i++) {
			bg.roll(pins);
		}
	}
	
	@Test
	//Test1
	void testGutter() {
		rollMany(20,0); //打0顆球
		assertEquals(0,bg.score());
	}
	
	@Test
	public void testAllOnes() {
		rollMany(20,1); //打到1顆球
		assertEquals(20,bg.score());
	}
	
	@Test
	public void testOneSapce() {
		rollSpace();  //space 10分
		bg.roll(3);   //第二局第一次 打3分
		              //第二局第一次 共16分 10+3+3
		
		rollMany(17,0);  //後面17次都打 0分
		assertEquals(16,bg.score());
	}
	
	@Test
	public void testOneStrike() {
		rollStrike();
		bg.roll(3);
		bg.roll(4);
		
		rollMany(16,0); //後面16次都打 0分   //因為第一局第一次打10分，第一局就不會打第二次
		assertEquals(24,bg.score());
	}
	@Test
	public void testPerfectGame() {
		rollMany(12,10);
		assertEquals(300,bg.score());
	}
	
	@BeforeEach  //一開始會執行我  //再執行Test1 //再執行@AfterEach
	public void before() {
		bg = new Game();
	}
	@AfterEach
	public void after() {
		bg = null;  //bg物件會被回收
	}
	
	private void rollSpace() {
		bg.roll(5);  //第一局第一次 打5分
		bg.roll(5);  //第一局第二次 打5分
	}
	private void rollStrike() {
		bg.roll(10); 
	}
}


class Game{
	private int score = 0;
	private int rolls[] = new int [21]; //最多可打21顆球 //每一個球打到幾瓶
	private int currentRoll = 0; //目前打到第幾苛求
 	
	public void roll(int pins) {
		score += pins;
		rolls[currentRoll++] = pins; //每一顆球，所打到的瓶數是多少
	}
	public int score() {
		int score = 0;
		int frameIndex = 0; //第一局的第一次丟球 //一局可丟兩次球	
		
		for(int frame=0; frame < 10; frame++) {  //frame局數 //第幾局
			if(isStike(frameIndex)) {
				//score += 10 + rolls[frameIndex+1] + rolls[frameIndex+2];
				score += 10 + strikeBouns(frameIndex);
				
				frameIndex++;
			}else if(isSpare(frameIndex)) { //目前這顆球與下一顆球加起來是10分的話
				//score += 10 + rolls[frameIndex+2];
				score += 10 + spareBouns(frameIndex);
				
				frameIndex+=2;
			}else {
				//score += rolls[frameIndex] + rolls[frameIndex+1] ; //加上下一局的瓶數
				score += sumOfBallIndexFrame(frameIndex);
				
				frameIndex += 2;
			}
		}
		return score;
	}
	
	private int sumOfBallIndexFrame(int frameIndex) {
		return rolls[frameIndex] + rolls[frameIndex+1];
	}
	private int spareBouns(int frameIndex) {
		return rolls[frameIndex+2];
	}
	private int strikeBouns(int frameIndex) {
		return rolls[frameIndex+1] + rolls[frameIndex+2];
	}
	
	private boolean isSpare(int frameIndex) {
		return rolls[frameIndex] + rolls[frameIndex+1] == 10;
	}
	private boolean isStike(int frameIndex) {
		return rolls[frameIndex] == 10;
	}
}