package com.kk.iterate.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kk.utils.AdjMatrixUtils;

/**
 * 
 * ����һ������Ⱥ��
 * @author Krise
 * 
 */
public final class ITMetapopulation {
	//
	//��Ϣ��
	public double lambda;//����������ʶ
	public double lambda_0;// 0-simplex
	public double lambda_1;// 1-simplex
	public double lambda_2;// 2-simplex
	public double theta_0;//
	public double theta_1;//
	private double theta_2;//
	public double delta;//A->U: ������Ϣ�ָ���
	//����㴫��
	public double beta_U;//US̬����ĸ�Ⱦ�� = 0.4
	public double gamma;//����ϵ��= 0.2
	public double beta_A; //AS̬����ĸ�Ⱦ�� = gamma * beta_U
	public double mu; //S̬����Ļָ���= 0.1
	//Ǩ�Ƹ���
	public double p;//= 0.2
	//�����С
	public int N;//= 100
	// ����һ���ڽӾ���洢patch�����ڹ�ϵ
	public double[][] adjMatrix_top;
	public double[][] adjMatrix_bot;
	// ת�ƾ���
	public double R_top[][];
	public double R_bot[][];

	//װ������patch������
	public ArrayList<ITPatch> iTPatches;
	// ��ű��Ϊid��patch�����ھ�patch��id˫�м���ӳ���
	public Map<Integer, ArrayList<Integer>> adjMap_top;

	/*************************************************************************************************************************************************
	/*************************************************************************************************************************************************
	/*************************************************************************************************************************************************
	 *
	 *
	 */
	public ITMetapopulation(String filePathTop, String filePathBot, int N, //
			double lambda, double lambda_0, double lambda_1, double lambda_2, //
			double theta_0, double theta_1, double theta_2, double delta, //
			double beta_U, double gamma, double mu, //
			double p, boolean isHomo) {
		//
		this.lambda = lambda;
		this.lambda_0 = lambda_0;
		this.lambda_1 = lambda_1;
		this.lambda_2 = lambda_2;
		this.theta_0 = theta_0;
		this.theta_1 = theta_1;
		this.theta_2 = theta_2;
		this.delta = delta;
		this.beta_U = beta_U;
		this.gamma = gamma;
		this.beta_A = gamma * beta_U;//
		this.mu = mu;
		this.p = p;
		this.N = N;
		//
		this.iTPatches = new ArrayList<ITPatch>(this.N);
		// ���ļ��ж���һ�� ���������ڽӾ���
		this.adjMatrix_top = AdjMatrixUtils.loadMatrixFromFile(filePathTop, N);
		this.adjMatrix_bot = AdjMatrixUtils.loadMatrixFromFile(filePathBot, N);
		// ��ȡת�ƾ���
		this.R_top = AdjMatrixUtils.get_R_Mtrix(adjMatrix_top);
		this.adjMap_top = AdjMatrixUtils.getNeigsMapByRows(adjMatrix_top);
		this.R_bot = AdjMatrixUtils.get_R_Mtrix(adjMatrix_bot);

		// ��ʼ������patch�� agent�� ������......
		if (isHomo) {
			this.seed_homo();
		} else {
			this.seed_heter();
		}
		// System.out.println("�ѳ�ʼ�����..............");
	}

	/**
	 * @param networkSize
	 */
	private void seed_homo() {
		//ÿ��patch�����˿�Ϊ100��
		int patchsize = 100;
		double seedsRatio = 0.01;
		//-------------------------------------
		for (int i = 0; i < this.N; i++) {
			ITPatch patch = new ITPatch(i);
			patch.population = patchsize;
			//---------------------------------
			patch.numberOfUS = patchsize * (1 - seedsRatio);
			patch.numberOfUI = patchsize * seedsRatio;
			patch.numberOfAS = 0;
			patch.numberOfAI = 0;
			//----------------------------------
			iTPatches.add(patch);
			//System.out.println(pat.population + "\t" + pat.numberOfUS + "\t" + pat.numberOfUI + "\t" + pat.numberOfAS + "\t" + pat.numberOfAI);
		}
	}//

	/**
	 * 
	 * ��ʼ������patch�� ���ն�����С��������......
	 */
	private void seed_heter() {
		double total = 100 * 100;
		double seedsRatio = 0.01;
		double totalWeight = AdjMatrixUtils.getTotalWeight(adjMatrix_bot);
		for (int r = 0; r < N; r++) {
			double outWeight = 0;
			for (int c = 0; c < N; c++) {
				outWeight = outWeight + adjMatrix_bot[r][c];
			}
			//double num = 0;
			double ratio = 0.0;
			if (outWeight > 0.0) {
				ratio = outWeight / totalWeight;
			}
			//----------------------------------
			ITPatch patch = new ITPatch(r);
			patch.population = total * ratio;
			//--------------------------------
			patch.numberOfUS = patch.population * (1 - seedsRatio);
			patch.numberOfUI = patch.population * seedsRatio;
			patch.numberOfAS = 0;
			patch.numberOfAI = 0;
			//-------------------------------
			iTPatches.add(patch);
		} //
	}//

	/************************************************************************************************************************************************
	************************************************************************************************************************************************
	************************************************************************************************************************************************
	*
	**/
	public void moveInteractReturn() {
		//������㼲����������
		updateNotInfectedMap();
		//Ȼ���������ʶ��������
		update_map_r_i();
		//ÿ��patch���η�������
		for (ITPatch patch_i : iTPatches) {
			//==1.======================================================================
			double qU_i = notInfectedMap.get(patch_i)[0];
			double qA_i = notInfectedMap.get(patch_i)[1];
			//==2.======================================================================
			//System.out.println("map_lambda_star.get(patch_i): "+map_lambda_star.get(patch_i)  ); 
			double rS_i = map_r_i.get(patch_i);//map_lambda_star.get(patch_i)==0ʱ�� û������
			double rI_i = (1.0 - lambda) * rS_i;
			//
			//==3 �������ݸ���==============================================================
			//System.out.println(r_i + "\t" + qU_i + "\t" + qA_i);

			//US
			double numberOf_US_next = patch_i.numberOfUS * rS_i * qU_i + //
			/*              */patch_i.numberOfUI * rI_i * mu + //
			/*              */patch_i.numberOfAS * delta * qU_i + //
			/*              */patch_i.numberOfAI * delta * mu;
			patch_i.numberOfUS = numberOf_US_next;
			//System.out.println( numberOf_US_next );
			//UI
			double numberOf_UI_next = patch_i.numberOfUS * rS_i * (1 - qU_i) + //
			/*              */patch_i.numberOfUI * rI_i * (1 - mu) + //
			/*              */patch_i.numberOfAS * delta * (1 - qU_i) + //
			/*              */patch_i.numberOfAI * delta * (1 - mu);
			patch_i.numberOfUI = numberOf_UI_next;
			//AS
			double numberOf_AS_next = patch_i.numberOfUS * (1 - rS_i) * qA_i + //
			/*              */patch_i.numberOfUI * (1 - rI_i) * mu + //
			/*              */patch_i.numberOfAS * (1 - delta) * qA_i + //
			/*              */patch_i.numberOfAI * (1 - delta) * mu;
			patch_i.numberOfAS = numberOf_AS_next;
			//AI
			patch_i.numberOfAI = patch_i.population - (numberOf_US_next + numberOf_UI_next + numberOf_AS_next);
			/*double numberOf_AI_next = patch_i.numberOfUS * (1 - rS_i) * (1 - qA_i) + //
					patch_i.numberOfUI * (1 - rI_i) * (1 - mu) + //
					patch_i.numberOfAS * (1 - delta) * (1 - qA_i) + //
					patch_i.numberOfAI * (1 - delta) * (1 - mu);
			patch_i.numberOfAI = numberOf_AI_next;*/
			//System.out.println(patch_i.population + "\t" + numberOf_US_next + "\t" + numberOf_UI_next + "\t" + numberOf_AS_next + "\t" + numberOf_AI_next);
		} //for patches
	}

	/**
	 * 
	 */
	private Map<ITPatch, Double[]> notInfectedMap = new LinkedHashMap<ITPatch, Double[]>(N);

	/**------------------------------------------------------------------------------------------------------------------------------------
	 * 
	 */
	private void updateNotInfectedMap() {
		//���map
		for (ITPatch patch_i : iTPatches) {////////
			int index_i = patch_i.id;

			//-1.��������============================================================================================================
			//// --------2.1û��Ǩ�ƣ�������patch�е�I̬�ĸ����Ⱦ(����������patch Ǩ�� ��patch��I̬����)--------��--R_bot��--------
			double _QUi = 1.0;
			double _QAi = 1.0;
			for (int row_j = 0; row_j < N; row_j++) {
				if (R_bot[row_j][index_i] > 0.0) {//�б�����
					ITPatch pat_j = iTPatches.get(row_j);
					_QUi = _QUi * Math.pow(1 - beta_U * pat_j.getCurrentRhoI(), p * R_bot[row_j][index_i] * pat_j.population);
					_QAi = _QAi * Math.pow(1 - beta_A * pat_j.getCurrentRhoI(), p * R_bot[row_j][index_i] * pat_j.population);
				} else if (row_j == index_i) {//����Ⱥ�ڲ�
					_QUi = _QUi * Math.pow(1 - beta_U * patch_i.getCurrentRhoI(), (1 - p) * patch_i.population);
					_QAi = _QAi * Math.pow(1 - beta_A * patch_i.getCurrentRhoI(), (1 - p) * patch_i.population);
				}

			} // for1
				////--------2.2)Ǩ�Ƶ�����patch�б���Ӧpatch�ڲ��е�I̬�����Ⱦ-------- ��--R_bot��----------
			double sigama_jtoN_QUj = 0.0;
			double sigama_jtoN_QAj = 0.0;
			// ���Tj(t), QjU(t), QjA(t)
			for (int col_j = 0; col_j < R_bot.length; col_j++) {//!!!!!
				if (R_bot[index_i][col_j] > 0.0) {// j i �б�����
					ITPatch patch_j = iTPatches.get(col_j);
					double _QUj = 1.0;
					double _QAj = 1.0;
					for (int row_l = 0; row_l < R_bot.length; row_l++) {//-------------------
						if (R_top[row_l][col_j] > 0.0) {//�б�����
							ITPatch pat_l = iTPatches.get(row_l);
							_QUj = _QUj * Math.pow(1 - beta_U * pat_l.getCurrentRhoI(), p * R_bot[row_l][col_j] * pat_l.population);
							_QAj = _QAj * Math.pow(1 - beta_A * pat_l.getCurrentRhoI(), p * R_bot[row_l][col_j] * pat_l.population);
						} else if (row_l == col_j) {
							_QUj = _QUj * Math.pow(1 - beta_U * patch_j.getCurrentRhoI(), (1 - p) * patch_j.population);
							_QAj = _QAj * Math.pow(1 - beta_A * patch_j.getCurrentRhoI(), (1 - p) * patch_j.population);
						}
					} //for----------------------------------
						//double _Tj =  _PI_1toN_T_2;
					sigama_jtoN_QUj = sigama_jtoN_QUj + R_bot[index_i][col_j] * _QUj;
					sigama_jtoN_QAj = sigama_jtoN_QAj + R_bot[index_i][col_j] * _QAj;
				}
			} //for2 !!!!!
			double qU_i = (1 - p) * _QUi + p * sigama_jtoN_QUj;
			double qA_i = (1 - p) * _QAi + p * sigama_jtoN_QAj;
			Double[] dd = new Double[2];
			dd[0] = qU_i;
			dd[1] = qA_i;
			//System.out.println(  qU_i +"-------   " +qA_i);
			notInfectedMap.put(patch_i, dd);
		}

	}//method

	/**
	 * 
	 */
	private Map<ITPatch, Double> map_r_i = new LinkedHashMap<ITPatch, Double>(N);

	/**--------------------------------------------------------------------------------------------------------------------
	 * @param id
	 * @return
	 */
	private void update_map_r_i() {
		for (ITPatch patch : iTPatches) {
			//0-simplex(��ϸ�����������)--------------------------------------------------------------
			double r_0_i = 1 - patch.getCurrentRhoA() * lambda_0;
			//1-simplex(OK)-----------------------------------------------------------------------
			List<Integer> neigIds = adjMap_top.get(patch.id);
			double r_1_i = 1.0;
			for (Integer neigId : neigIds) {
				r_1_i = r_1_i * (1 - iTPatches.get(neigId).getCurrentRhoA() * lambda_1);
			}
			//2-simplex���е����⣩-------------------------------------------------------------------
			double r_2_i = 1.0;
			//int count = 0;
			for (int j = 0; j < neigIds.size(); j++) {
				for (int k = j + 1; k < neigIds.size(); k++) {
					if (adjMatrix_top[neigIds.get(j)][neigIds.get(k)] > 0.0) {//
						r_2_i = r_2_i * (1 - iTPatches.get(neigIds.get(j)).getCurrentRhoA() * iTPatches.get(neigIds.get(k)).getCurrentRhoA() * lambda_2);
						//count++;
					}
				}
			}
			//---------------------------------------------------------------------------------------
			double r_i = theta_0 * r_0_i + (1 - theta_0) * theta_1 * r_1_i + (1 - theta_0) * (1 - theta_1) * r_2_i;
			//--------------------------------------------------------------------------------------
			map_r_i.put(patch, r_i);
		}
	}

	/*********************************************************************************************************************************************
	 *********************************************************************************************************************************************
	 *********************************************************************************************************************************************
	 * 
	 * ͳ������
	 */
	public String statistics() {
		double total_US = 0;
		double total_UI = 0;
		double total_AS = 0;
		double total_AI = 0;
		double total = 0;
		for (ITPatch patch : iTPatches) {
			total_US = total_US + patch.numberOfUS;
			total_UI = total_UI + patch.numberOfUI;
			total_AS = total_AS + patch.numberOfAS;
			total_AI = total_AI + patch.numberOfAI;
			total = total + patch.population;
		}
		String str = //
				/*    */total_US / total + "\t" + //
						total_UI / total + "\t" + //
						total_AS / total + "\t" + //
						total_AI / total + "\t" + //
						(total_US + total_UI) / total + "\t" + // U
						(total_AS + total_AI) / total + "\t" + // A
						(total_US + total_AS) / total + "\t" + // S
						(total_UI + total_AI) / total // I  
		;
		//System.out.println(total_US + "\t" + total_UI + "\t" + total_AS + "\t" + total_AI + "\t" + total);
		return str;
	}// String statistics

}//Class
