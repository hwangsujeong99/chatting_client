package chatting_client;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import global.global;
import popup.Show_info;

public class Chat_win extends JFrame {

	FriendList f_list;
	FriendSearch f_search;
	String id = "";
	JTree jtree_list;
	JTree jtree_search;

	boolean isAlpha = false;
	JPanel contentPane = new JPanel();
	private JTextField textField;

	static String selectedFriend = "";
	static String selectedSearchFriend = "";

	ArrayList<String> searchList = new ArrayList<>();
	ArrayList<String> f_arr_list = new ArrayList<>();

	String covid_total_check = "";
	String covid_dead_cnt = "";
	String covid_care_cnt = "";
	
	public void load_friend_list() {

		String send_msg = "friendList/" + id;
		ChatClient cc = new ChatClient(global.s_ipaddr, global.s_port, send_msg);
		String recv_msg = cc.recvMsg();

		System.out.println("friend_list" + recv_msg);

		String friend = recv_msg.split("/")[3];
		friend = friend.replaceAll(Pattern.quote("["), "");
		friend = friend.replaceAll(Pattern.quote("]"), "");

		String[] f_tmp = friend.split(",");

		f_arr_list = new ArrayList<>();
		
		for (int i = 0; i < f_tmp.length; i++) {
			f_arr_list.add(f_tmp[i]);
		}

	}
	
	public void search_friend_list() {
		
		searchList = new ArrayList<>();
		
		String send_msg = "searchFriend/" + id;
		ChatClient cc = new ChatClient(global.s_ipaddr, global.s_port, send_msg);
		String recv_msg = cc.recvMsg();

		System.out.println("sssss"+recv_msg);

		String friend = recv_msg.split("/")[1];
		friend = friend.replaceAll(Pattern.quote("["), "");
		friend = friend.replaceAll(Pattern.quote("]"), "");

		String[] f_tmp = friend.split(",");

		for (int i = 0; i < f_tmp.length; i++) {
			searchList.add(f_tmp[i].replace(" ", ""));
		}
	
		System.out.println(">>"+searchList);
		
		
	}

	public void api_call() {
		
		BufferedReader br = null;
		// DocumentBuilderFactory 생성
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		Document doc = null;
		try {
			// OpenApi호출
			
			String service_key = "aK0Mw%2FLu3XPj3JmuUbnUy6wEmScBmbjH1NSjFp4S0xaPYm8Yi1wD2i76eNbMR2TP09xp3GNS%2BkGZ6LRpB7QJlA%3D%3D";
			
	        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+ service_key); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode("20200310", "UTF-8")); /*검색할 생성일 범위의 시작*/
	        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode("20200315", "UTF-8")); /*검색할 생성일 범위의 종료*/
			
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			// 응답 읽기
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
			String result = "";
			String line;
			while ((line = br.readLine()) != null) {
				result = result + line.trim();// result = URL로 XML을 읽은 값
			}

			// xml 파싱하기
			InputSource is = new InputSource(new StringReader(result));
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			// XPathExpression expr = xpath.compile("/response/body/items/item");
			XPathExpression expr = xpath.compile("//items/item");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				NodeList child = nodeList.item(i).getChildNodes();
				for (int j = 0; j < child.getLength(); j++) {
					Node node = child.item(j);
					
					if(node.getNodeName().contains("deathCnt")) {
						covid_dead_cnt = node.getTextContent();
					}
					
					if(node.getNodeName().contains("careCnt")) {
						covid_care_cnt = node.getTextContent();
					}
					

					if(node.getNodeName().contains("accExamCnt")) {
						covid_total_check = node.getTextContent();
					}
//					
//					System.out.println("현재 노드 이름 : " + node.getNodeName());
//					System.out.println("현재 노드 타입 : " + node.getNodeType());
//					System.out.println("현재 노드 값 : " + node.getTextContent());
//					System.out.println("현재 노드 네임스페이스 : " + node.getPrefix());
//					System.out.println("현재 노드의 다음 노드 : " + node.getNextSibling());
//					System.out.println("");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public Chat_win(String id) {

		// api call (COVID 19)
		api_call();
		
		this.id = id;
		// 친구 목록 불러오기
		load_friend_list();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 509, 874);
		setTitle("Welcome your ID is: "+ id);
		
		setVisible(true);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton btn_refresh = new JButton("refresh");
		btn_refresh.setBounds(356, 137, 89, 24);
		btn_refresh.addActionListener(e -> doRefresh());
		contentPane.setLayout(null);
		contentPane.add(btn_refresh);

		/* friend search */
		f_search = new FriendSearch(searchList);
		jtree_search = new JTree(f_search);
		JScrollPane scrollPane_search = new JScrollPane(jtree_search);
		scrollPane_search.setBounds(5, 64, 440, 72);
		contentPane.add(scrollPane_search);

		// friend search set pop-up
		JPopupMenu searchPopup = new JPopupMenu();
		JMenuItem s_add = new JMenuItem("add friend");
		JMenuItem s_info = new JMenuItem("show info");
		searchPopup.add(s_add);
		searchPopup.add(s_info);

		jtree_search.setComponentPopupMenu(searchPopup);
		initPopupListener(jtree_search, searchPopup);

		/* friend list */
		f_list = new FriendList(f_arr_list);
		f_list.load(f_arr_list);
		jtree_list = new JTree(f_list);
		JScrollPane scrollPane_list = new JScrollPane(jtree_list);
		scrollPane_list.setBounds(5, 167, 440, 597);
		contentPane.add(scrollPane_list);

		// friend list set pop-up
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem i = new JMenuItem("chat");
		popupMenu.add(i);
		jtree_list.setComponentPopupMenu(popupMenu);
		initPopupListener(jtree_list, popupMenu);

		textField = new JTextField();
		textField.setBounds(5, 30, 348, 19);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btn_search = new JButton("search");
		btn_search.setBounds(356, 27, 89, 25);
		contentPane.add(btn_search);

		JLabel lblNewLabel = new JLabel("Friend List");
		lblNewLabel.setBounds(15, 142, 121, 15);
		contentPane.add(lblNewLabel);

		JLabel lblSearchFriend = new JLabel("Search Friend");
		lblSearchFriend.setBounds(15, 3, 121, 15);
		contentPane.add(lblSearchFriend);

		JLabel lblNewLabel_1 = new JLabel("API Call Result:");
		lblNewLabel_1.setBounds(15, 776, 440, 24);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_1.setText("COVID TOTAL:" + covid_total_check + " CARE: " + covid_care_cnt + " DEATH: " +covid_dead_cnt);

		// 1-1. 검색 버튼 클릭
		btn_search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String search_name = textField.getText();
				// Send message
				doSearch(search_name);
			}

		});

		// 1-2. search add_friend/show info 대상 선택
		jtree_search.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jtree_search
						.getLastSelectedPathComponent();
				try {
					selectedSearchFriend = selectedNode.toString();
					System.out.println("@@" + selectedSearchFriend);
				} catch (Exception ee) {

				}

			}
		});

		// 1-2. add friend Button 클릭
		s_add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(selectedFriend);

				if (!selectedSearchFriend.equals("")) {
					
					String send_msg = "addfriend/" + id +"/"+ selectedSearchFriend;
					ChatClient cc = new ChatClient(global.s_ipaddr, global.s_port, send_msg);
					
					// 친구 목록 불러오기
					load_friend_list();
					
				}
			}
		});

		// 1-3. show friend info Button 클릭
		s_info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String send_msg = "showinfo/" + selectedSearchFriend;
				ChatClient cc = new ChatClient(global.s_ipaddr, global.s_port, send_msg);
				String recv_msg = cc.recvMsg();
				
				if (!selectedSearchFriend.equals("")) {
					// 친구 정보를 받아와서 뿌려줄꺼야?
					
					String nickname = recv_msg.split("/")[1];
					String name = recv_msg.split("/")[2];
					String email = recv_msg.split("/")[3];
					String birth = recv_msg.split("/")[4];
					String status = recv_msg.split("/")[5];

					new Show_info(nickname, name, email, birth, status);
					System.out.println("show friend info" + selectedSearchFriend);
				}
			}
		});

		// 2-1. Chating Button 클릭
		i.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				System.out.println(selectedFriend);

				if (!selectedFriend.equals("")) {
					new ChatClient_GUI(id, selectedFriend);
				}

			}

		});

		// 2. Chatting 대상 선택
		jtree_list.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jtree_list
						.getLastSelectedPathComponent();
				try {
					selectedFriend = selectedNode.toString();
				} catch (Exception ee) {

				}

			}
		});

	}

	private void doSearch(String search_name) {

		search_friend_list();
		f_search.load(search_name, searchList);
	}

	private void doRefresh() {
		f_list.load(f_arr_list);
	}

	private static void searchPopupListener(JTree tree, JPopupMenu popupMenu) {
		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// get selected node's rectangle
				Rectangle rect = tree.getPathBounds(tree.getSelectionPath());
				Arrays.stream(popupMenu.getComponents()).forEach(c -> c.setEnabled(rect != null));
				if (rect == null) {
					return;
				}

				Point p = new Point(rect.x + rect.width / 2, rect.y + rect.height);
				SwingUtilities.convertPointToScreen(p, tree);
				popupMenu.setLocation(p.x, p.y);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}

		});
	}

	private static void initPopupListener(JTree tree, JPopupMenu popupMenu) {
		popupMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// get selected node's rectangle
				Rectangle rect = tree.getPathBounds(tree.getSelectionPath());
				Arrays.stream(popupMenu.getComponents()).forEach(c -> c.setEnabled(rect != null));
				if (rect == null) {
					return;
				}

				Point p = new Point(rect.x + rect.width / 2, rect.y + rect.height);
				SwingUtilities.convertPointToScreen(p, tree);
				popupMenu.setLocation(p.x, p.y);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}

		});
	}
}

class FriendSearch extends DefaultTreeModel {

	DefaultMutableTreeNode rootNode = null;
	ArrayList<String> searchList;

	public FriendSearch(ArrayList<String> searchList) {
		super(new DefaultMutableTreeNode("search_result"));
		rootNode = (DefaultMutableTreeNode) getRoot();
		this.searchList = searchList;
		// TODO Auto-generated constructor stub
	}

	public void load(String s_name, ArrayList<String> searchList) {

		this.searchList = searchList;
		
		rootNode.removeAllChildren();

		DefaultMutableTreeNode childNode;

		for (int i = 0; i < searchList.size(); i++) {

			// 특정 문자열을 포함하는 경우
			if (searchList.get(i).contains(s_name)) {
				childNode = new DefaultMutableTreeNode(searchList.get(i));
				rootNode.add(childNode);
			}

		}
		nodeStructureChanged(rootNode);

	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	
}

class FriendList extends DefaultTreeModel {

	DefaultMutableTreeNode rootNode = null;
	ArrayList<String> f_arr_list;

	public FriendList(ArrayList<String> f_arr_list) {
		super(new DefaultMutableTreeNode("friend_list"));
		rootNode = (DefaultMutableTreeNode) getRoot();
		this.f_arr_list = f_arr_list;

	}

	public void load(ArrayList<String> f_arr_list) {

		this.f_arr_list = f_arr_list;

		
		rootNode.removeAllChildren();

		DefaultMutableTreeNode childNode;

		for (int i = 0; i < f_arr_list.size(); i++) {

			childNode = new DefaultMutableTreeNode(f_arr_list.get(i));
			rootNode.add(childNode);
		}

		nodeStructureChanged(rootNode);

	}

	public TreeNode getRootNode() {
		return rootNode;
	}
}