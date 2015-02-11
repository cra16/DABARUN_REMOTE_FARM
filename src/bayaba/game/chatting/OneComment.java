package bayaba.game.chatting;

/*
 * isOther�� true�̸� �ٸ� ����� �� ��. ������ ǥ��
 * 
 * */


public class OneComment {
	public boolean isOther;
	public String comment;

	
	//1:1ä���̴ϱ� �����κ��� �Դ����� ǥ�� ���� �ʾƵ� ��
	//�����κ��� �Դ��� ǥ���Ϸ��� param �ϳ� �� ���� �־�����ҵ�
	
	public OneComment(boolean isOther, String comment) {
		super();
		this.isOther = isOther;
		this.comment = comment;
	}

}