package com.kk.iterate.entity;

/**
 * ����һ��patch
 * 
 * @author Krise
 * 
 */
public class ITPatch {
	//
	public int id;// Ϊÿ����Ⱥ���
	//
	public double numberOfUS = 0.0;// US̬�ĸ�����
	public double numberOfUI = 0.0;// UI̬�ĸ�����
	public double numberOfAS = 0.0;// AS̬�ĸ�����
	public double numberOfAI = 0.0;// AI̬�ĸ�����
	public double population = 0.0;// ���˿���

	/**
	 * @param id
	 */
	public ITPatch(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public double getCurrentRhoA() {
		if (population > 0) {
			return (numberOfAS + numberOfAI) / population;
		} else {
			return 0.0;
		}
	}

	/**
	 * 
	 * �õ���ǰpatch I̬����ı���
	 * @return
	 */
	public double getCurrentRhoI() {
		if (population > 0) {
			return (numberOfUI + numberOfAI) / population;
		} else {
			return 0.0;
		}//
	}//

}//Class
