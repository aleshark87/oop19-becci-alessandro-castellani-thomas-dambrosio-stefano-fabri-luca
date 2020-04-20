package model;

import java.util.List;

public class StandardPlayerImpl implements StandardPlayer{
	
	public static final int POSITION_BOUNDARY = 8;
	public static final int BARRIERS_NUMBER = 10;
	private String nickname;
	private Pair<Integer, Integer> position;
	private Integer remainingBarriers;
	private Integer XfinishLine;
	
	public StandardPlayerImpl(String nickname, Pair<Integer, Integer> position) {
		super();
		this.nickname = nickname;
		this.position = position;
		this.remainingBarriers = BARRIERS_NUMBER;
		this.setXFinishLine(POSITION_BOUNDARY - this.position.getX());
	}

	@Override
	public void move(Direction direction, Type type) {
		final int offset = type.equals(Type.NORMAL) ? 1 : 2;
		switch(direction) {
		case LEFT:
			this.position = new Pair<>(this.getCurrentPosition().getX() - offset, this.getCurrentPosition().getY());
			break;
		case RIGHT:
			this.position = new Pair<>(this.getCurrentPosition().getX() + offset, this.getCurrentPosition().getY());
			break;
		case TOP:
			this.position = new Pair<>(this.getCurrentPosition().getX(), this.getCurrentPosition().getY() - offset);
			break;
		case DOWN:
			this.position = new Pair<>(this.getCurrentPosition().getX(), this.getCurrentPosition().getY() + offset);
			break;
		}
	}

	@Override
	public String getNickname() {
		return this.nickname;
	}

	@Override
	public Integer getRemainingBarriers() {
		return this.remainingBarriers;
	}

	@Override
	public Pair<Integer, Integer> getCurrentPosition() {
		return this.position;
	}
	
	@Override
	public boolean isWinner() {
		return this.position.getX().equals(POSITION_BOUNDARY);
	}

	@Override
	public void setPossibleMoves(List<Pair<Integer, Integer>> positions) {
		// TODO Auto-generated method stub
	}

	private void setXFinishLine(Integer x) {
		this.XfinishLine = x;
	}
	
}
