package com.kk.utils;

import java.io.*;
import java.util.*;

/**
 * @author Krise
 * 
 */
public class AdjMatrixUtils {

	/**
	 * ����һ���������
	 * 
	 * @param size
	 * @param maxWeight
	 * @return
	 */
	public static int[][] getRandomMtrix(int size, int maxWeight) {
		int[][] W = new int[size][size];
		Random rand = new Random();
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				W[r][c] = rand.nextInt(maxWeight) + 1;
			}
		}//
		return W;
	}

	/**
	 *  ��������ڽӾ��������Ȩ��
	 * 
	 * @param network
	 * @param maxWeight
	 * @param isDigraph
	 * @return
	 */
	public static int[][] get_W_MtrixByAdjMtrix(int network[][], int maxWeight, boolean isDigraph) {
		int[][] W = new int[network.length][network.length];
		if (isDigraph) {//����ͼ
			for (int r = 0; r < network.length; r++) {
				for (int c = 0; c < network.length; c++) {
					if (network[r][c] != 0) {
						W[r][c] = new Random().nextInt(maxWeight) + 1;
						W[c][r] = new Random().nextInt(maxWeight) + 1;
					}
				}
			}
		} else {//����ͼ
			for (int r = 0; r < network.length; r++) {
				for (int c = 0; c < r; c++) {
					if (network[r][c] != 0) {
						W[r][c] = W[c][r] = new Random().nextInt(maxWeight) + 1;
					}
				}
			}//
		}

		return W;
	}

	/**
	 * ���ת�ƾ���-Rij
	 * 
	 * @param matrix
	 * @return
	 */
	public static double[][] get_R_Mtrix(int[][] matrix) {
		double[][] R = new double[matrix.length][matrix.length];
		double sumRow = 0;
		for (int r = 0; r < matrix.length; r++) {
			sumRow = 0;
			for (int c = 0; c < matrix[r].length; c++) {
				sumRow = sumRow + matrix[r][c];
			}
			if (sumRow > 0) {
				for (int c = 0; c < matrix[r].length; c++) {
					R[r][c] = matrix[r][c] / (1.0 * sumRow);
				}
			}
		}
		return R;
	}

	/**
	 * ���ת�ƾ���-Rij ---���ط���
	 * 
	 * @param matrix
	 * @return
	 */
	public static double[][] get_R_Mtrix(double[][] matrix) {
		double[][] R = new double[matrix.length][matrix.length];
		double sumRow = 0;
		for (int r = 0; r < matrix.length; r++) {
			sumRow = 0;
			for (int c = 0; c < matrix[r].length; c++) {
				sumRow = sumRow + matrix[r][c];
			}
			if (sumRow > 0) {
				for (int c = 0; c < matrix[r].length; c++) {
					R[r][c] = matrix[r][c] / (1.0 * sumRow);
				}
			}
		}
		return R;
	}

	/**
	 * ���ת�ƾ���ĸ��ʴ�С�����ۼӷֲ�
	 * 
	 * @param matrix
	 * @return
	 */
	public static double[][] get_R_Dis_Mtrix(double[][] R) {
		double[][] R_des = new double[R.length][R.length];
		for (int r = 0; r < R.length; r++) {
			double weight = 0.0;
			for (int c = 0; c < R.length; c++) {
				if (R[r][c] != 0) {
					weight = weight + R[r][c];
					R_des[r][c] = weight;
				}// if
			}
		}
		return R_des;
	}

	/**
	 * ��ȡ�ڽӾ����ܶ���
	 * 
	 * @param adjMatrix
	 */
	public static int getTotalDegree(int[][] adjMatrix) {
		int count = 0;

		for (int i = 0; i < adjMatrix.length; i++)
			for (int j = 0; j < adjMatrix[i].length; j++) {
				count = count + (adjMatrix[i][j] != 0 ? 1 : 0);
			}
		return count;
	}

	/**
	 * ��ȡ������Ȩ��
	 * 
	 * @param adjMatrix
	 */
	public static double getTotalWeight(double[][] adjMatrix) {
		double totalWeight = 0;
		for (int r = 0; r < adjMatrix.length; r++) {
			for (int c = 0; c < adjMatrix[r].length; c++) {
				totalWeight = totalWeight + adjMatrix[r][c];
			}
		}
		return totalWeight;
	}

	/**
	 * ����̨��ӡ����
	 * 
	 * @param matrix
	 */
	public static void printAdjacentMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * ����̨��ӡ����---���ط���
	 * 
	 * @param matrix
	 */
	public static void printAdjacentMatrix(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(String.format("%.3f", matrix[i][j]) + " ");
			}
			System.out.println();
		}
	}

	/**
	 * ���ĳһ���ڵ���ھӽڵ�
	 * 
	 * @param rId
	 * @param adjacentMatrix
	 * @return
	 */
	public static ArrayList<Integer> getNodeNeighborsByNodeId(double[][] adjacentMatrix, int nodeId) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int c = 0; c < adjacentMatrix[nodeId].length; c++) {
			if (adjacentMatrix[nodeId][c] != 0) {
				list.add(c);
			}
		}
		return list;
	}

	/**
	 * ��ø����ڵ���ڽӽڵ�
	 * 
	 * @param adjacentMatrix
	 * @return
	 */
	public static Map<Integer, ArrayList<Integer>> getNeigsMapByRows(double[][] adjacentMatrix) {
		Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		for (int row = 0; row < adjacentMatrix.length; row++) {
			map.put(row, getNodeNeighborsByNodeId(adjacentMatrix, row));
		}
		return map;
	}

	/**
	 * �жϾ����Ƿ�Ϊ�Գ���
	 * 
	 * @param adjacentMatrix
	 * @return
	 */
	public static boolean isSymmetric(int[][] adjacentMatrix) {
		for (int r = 0; r < adjacentMatrix.length; r++) {
			for (int c = 0; c <= r; c++) {
				if (adjacentMatrix[r][c] != adjacentMatrix[c][r])
					return false;
			}
		}
		return true;
	}

	/**
	 * �����ڽӾ����ƽ���ȷֲ�
	 */
	public static void calcDegreeDistribution(int[][] adjacentMatrix) {
		int networkSize = adjacentMatrix.length;
		int[] degree = new int[networkSize];
		int i, j;
		for (i = 0; i < networkSize; i++)
			for (j = 0; j < networkSize; j++) {
				degree[i] = degree[i] + (adjacentMatrix[i][j] != 0 ? 1 : 0);
			}
		int sumDegree = 0;
		for (i = 0; i < degree.length; i++)
			sumDegree += degree[i];
		System.out.println("�ܶ���=" + sumDegree + " ƽ����=" + sumDegree * 1.0 / networkSize);
		double[] statistic = new double[networkSize];
		for (i = 0; i < networkSize; i++)
			statistic[degree[i]]++;
		// double indentify = 0.0;
		for (i = 0; i < networkSize; i++) {
			double t = statistic[i] / networkSize;
			// indentify += t;
			System.out.println("��Ϊ�� " + i + " �ĸ���Ϊ��" + (int) statistic[i] + "  Ƶ�ʣ� " + t);
		}
		// System.out.println("����֮��:   " + indentify);
	}

	/**
	 * ���ļ��ж�������ڴ���
	 * 
	 * @param filePath
	 * @return
	 */
	public static double[][] loadMatrixFromFile(String filePath, int matrixSize) {
		double[][] matrix = new double[matrixSize][matrixSize];
		BufferedReader bufr = null;
		try {
			bufr = new BufferedReader(new FileReader(filePath));
			String line = null;
			int row = 0;
			while ((line = bufr.readLine()) != null) {
				String strs[] = line.split("\t");
				int col = 0;
				for (int i = 0; i < strs.length; i++) {
					matrix[row][col++] = Double.parseDouble(strs[i]);
				}
				row++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (bufr != null) {
				try {
					bufr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return matrix;
	}

	/**
	 * ���ڽӾ���д���ļ���
	 * 
	 * @param adjacentMatrix
	 */
	public static void exportMatrix2File(int[][] adjacentMatrix, String filePath) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(filePath), true);
			for (int row = 0; row < adjacentMatrix.length; row++) {
				for (int col = 0; col < adjacentMatrix.length - 1; col++) {
					out.print(adjacentMatrix[row][col] + "\t");
				}// for
				out.println(adjacentMatrix[row][adjacentMatrix[row].length - 1]);
			}// for1
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}// method

}//C
