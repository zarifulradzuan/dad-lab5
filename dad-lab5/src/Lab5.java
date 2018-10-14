import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Font;

public class Lab5 {

	private JFrame frame;
	private JTextField textFieldId;
	private JTextField textFieldName;
	private JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lab5 window = new Lab5();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Lab5() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 657, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblStudLoginId = new JLabel("Stud Login Id");
		lblStudLoginId.setBounds(75, 36, 120, 14);
		frame.getContentPane().add(lblStudLoginId);
		
		JLabel lblStudName = new JLabel("Stud Name");
		lblStudName.setBounds(75, 68, 120, 14);
		frame.getContentPane().add(lblStudName);
		
		textFieldId = new JTextField();
		textFieldId.setBounds(205, 33, 156, 20);
		frame.getContentPane().add(textFieldId);
		textFieldId.setColumns(10);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(205, 65, 156, 20);
		frame.getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread searchThread= new Thread() {
					
					
					public void run() {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "searchStudent"));
						params.add(new BasicNameValuePair("studId", textFieldId.getText()));
						params.add(new BasicNameValuePair("studName", ("%"+textFieldName.getText()+"%")));
						
						String strUrl = "http://localhost/webServiceJSON/genericWebService.php";
						JSONArray jArr = makeHttpRequest(strUrl,"POST",params);
						JSONObject jsnObj=null;
						String strSetText="";
						try {
							for(int i = 0 ;i<jArr.length();i++) {
								jsnObj = jArr.getJSONObject(i);
								String studFirstName = jsnObj.getString("firstname");
								String studLastName = jsnObj.getString("lastname");
								String studLogin = jsnObj.getString("login");
								String studLastLog = jsnObj.get("last_login_on").toString();
								
								strSetText += "First Name :"+studFirstName+
										" || Last Name :"+studLastName+
										" || Login ID :"+studLogin+
										" || Last Login :"+studLastLog+"\n";
							}
							textArea.setText(strSetText);
					
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					public JSONArray makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONArray jArr = null;
						
						try {
							if(method == "POST") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(strUrl);
								httpPost.setEntity(new UrlEncodedFormEntity(params));
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								strUrl+="?"+paramString;
								HttpGet httpGet = new HttpGet(strUrl);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jArr = new JSONArray(json);
							
						}	catch(JSONException e) {
							try {
								jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jArr;
					}
					
				};
				
				searchThread.start();
			}
		});
		btnSearch.setBounds(469, 32, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 174, 621, 101);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Open Sans Light", Font.PLAIN, 13));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
	}
}