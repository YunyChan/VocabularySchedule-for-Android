package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewModeActivity extends Activity{	
	ReviewModeActivity activityContext=this;
	private MainApp mApp;
	
	TextView tv_modeName;
	ImageButton ibtn_deleteMode,ibtn_addMode;
	ImageButton ibtn_reset,ibtn_addPeriod,ibtn_save,ibtn_editModeName,ibtn_undo;
	
	//
	ListView lv_periods;
	ReviewModeListAdapter lv_peroids_adapter;

	
	//spn_books��ʹ�õı���
	Spinner spn_modes;
	ArrayList<String> spn_modes_al;
	ArrayAdapter<String> spn_modes_adapter;
	Vector<Integer> modesIdVec;//Value-ModeId
	Integer curSelectedModeId;
	boolean isCancelSelect;
	
	//-2��������,-1����ɾ����>0�����޸�
	private Vector<Operating> operatingSequence;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_mode);
		
		mApp=(MainApp)getApplication();
			
		tv_modeName=(TextView)findViewById(R.id.tv_modeName);
		
		
		lv_periods=(ListView)findViewById(R.id.lv_periods);	
		lv_peroids_adapter=new ReviewModeListAdapter();		
		lv_periods.setAdapter(lv_peroids_adapter);
		
		operatingSequence=new Vector<Operating>();
		
		InitModesSpinner();
		InitIbtnAddMode();
		InitIbtnDeleteMode();	
		InitIbtnEditModeName();
		InitIbtnUndo();
		InitIbtnReset();
		InitIbtnAddPeriod();
		InitIbtnSave();
	}
	
	private void InitModesSpinner(){
		isCancelSelect=false;
		spn_modes_al=new ArrayList<String>();
		spn_modes=(Spinner)findViewById(R.id.spn_modes);
		ResetModesSpinnerAdapter();
		spn_modes.setOnItemSelectedListener(new OnItemSelectedListener() {
			int ItemSelectedPosition;
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				ItemSelectedPosition=arg2;
				if(operatingSequence.size()>0){
					AlertDialog.Builder builder = new Builder(activityContext);
					builder.setTitle("��û���������");
					builder.setIcon(android.R.drawable.ic_dialog_info);
					TextView tv_saveWarning=new TextView(activityContext);
					tv_saveWarning.setText("ȷ��������Ա�ģʽ�����в�����");
					tv_saveWarning.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					builder.setView(tv_saveWarning);
					builder.setPositiveButton("������", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							operatingSequence.clear();
							ChangeModeListView(ItemSelectedPosition);
							arg0.dismiss();
						}
						
					});
					builder.setNegativeButton("����", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isCancelSelect=true;
							ChangeModeListView(modesIdVec.indexOf(curSelectedModeId));
							dialog.dismiss();
						}
						
					});
					builder.create().show();
				}else{
					if (!isCancelSelect)
						ChangeModeListView(ItemSelectedPosition);
					else
						isCancelSelect=false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void InitIbtnAddMode() {
		ibtn_addMode=(ImageButton)findViewById(R.id.ibtn_addMode);
		ibtn_addMode.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int newId=mApp.modes.size();
						ReviewMode newMode=new ReviewMode(new Vector<Integer>(),"�¸�ϰģʽ",newId);
						spn_modes_adapter.add(newMode.name);
						int newModeSpinnerIndex=spn_modes_adapter.getCount()-1;
						spn_modes.setSelection(newModeSpinnerIndex);
						curSelectedModeId=Integer.valueOf(newId);
						mApp.modes.put(curSelectedModeId, newMode);
						modesIdVec.add(curSelectedModeId);
					}
				});
	}
	
	private void InitIbtnDeleteMode() {
		ibtn_deleteMode=(ImageButton)findViewById(R.id.ibtn_deleteMode);
		ibtn_deleteMode.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int totalSelectedBooks=mApp.modes.get(curSelectedModeId).selectedBookIds.size();
						if(totalSelectedBooks==0){
							mApp.modes.remove(curSelectedModeId);
							spn_modes_adapter.clear();
							ResetModesSpinnerAdapter();
						}else
							Toast.makeText(getApplicationContext(),"����"+totalSelectedBooks+"����ռ�ø�ģʽ",Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	private void InitIbtnEditModeName() {
		ibtn_editModeName=(ImageButton)findViewById(R.id.ibtn_editModeName);
		ibtn_editModeName.setOnClickListener(new OnClickListener() {
			EditText et_modeName;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(activityContext);
				builder.setTitle("ģʽ������");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_modeName=new EditText(activityContext);
				et_modeName.setText(tv_modeName.getText());
				builder.setView(et_modeName);
				builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String newModeName=et_modeName.getText().toString();
						mApp.modes.get(curSelectedModeId).name=newModeName;
						tv_modeName.setText(newModeName);
						int curSpinnerIndex=spn_modes.getSelectedItemPosition();
						spn_modes_adapter.clear();
						ResetModesSpinnerAdapter();
						spn_modes.setSelection(curSpinnerIndex);
						arg0.dismiss();
					}
					
				});
				builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
	}
	
	private void InitIbtnUndo() {
		
		ibtn_undo=(ImageButton)findViewById(R.id.ibtn_undo);
		ibtn_undo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(operatingSequence.size()>0)
					CancelLastOperating();
			}
		});
	}
	
	private void InitIbtnReset() {
		ibtn_reset=(ImageButton)findViewById(R.id.ibtn_reset);
		ibtn_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				while(operatingSequence.size()>0)
					CancelLastOperating();
			}
		});
	}
	
	private void InitIbtnAddPeriod() {
		ibtn_addPeriod=(ImageButton)findViewById(R.id.ibtn_addPeriod);
		ibtn_addPeriod.setOnClickListener(new OnClickListener() {
			EditText et_period;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(activityContext);
				builder.setTitle("��"+(lv_peroids_adapter.getCount()+1)+"���ڵļ������");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_period=new EditText(activityContext);
				et_period.setHint("����������");
				et_period.setKeyListener(new DigitsKeyListener(false,true));
				builder.setView(et_period);
				builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Operating operating=new Operating(1,lv_peroids_adapter.getCount());
						operatingSequence.add(operating);
						String et_period_contant=et_period.getText().toString();
						lv_peroids_adapter.AddNewPeriod(lv_peroids_adapter.getCount(),Integer.valueOf(Integer.parseInt(et_period_contant)));
						arg0.dismiss();
					}
					
				});
				builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
	}
	
	private void InitIbtnSave() {
		ibtn_save=(ImageButton)findViewById(R.id.ibtn_save);
		ibtn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetAllOperatings();
			}
		});
	}
	
	private void ResetModesSpinnerAdapter(){
		modesIdVec=new Vector<Integer>();
		Enumeration<ReviewMode> modesEnu=mApp.modes.elements();
		while (modesEnu.hasMoreElements()) {
			ReviewMode curMode = (ReviewMode) modesEnu.nextElement();
			spn_modes_al.add(curMode.name);
			modesIdVec.add(Integer.valueOf(curMode.id));
		}
		spn_modes_adapter=new
				ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spn_modes_al);
		spn_modes_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_modes.setAdapter(spn_modes_adapter);
	}
	
	private void CancelLastOperating() {
		Operating lastOperating=operatingSequence.lastElement();
		switch (lastOperating.operate) {
		case 0:
			lv_peroids_adapter.AddNewPeriod(lastOperating.position, lastOperating.value);
			break;
		case 1:
			lv_peroids_adapter.RemovePeriod(lastOperating.position);
			break;
		case 2:
			lv_peroids_adapter.SetPeriod(lastOperating.position, lastOperating.value);
			break;
		default:
			break;
		}
		operatingSequence.remove(operatingSequence.size()-1);
	}
	
	private void SetAllOperatings() {
		Iterator<Operating> operatingIt=operatingSequence.iterator();
		ReviewMode curMode=mApp.modes.get(curSelectedModeId);
		while (operatingIt.hasNext()) {
			ReviewModeActivity.Operating operating = (ReviewModeActivity.Operating) operatingIt
					.next();
			switch (operating.operate) {
			case 0:
				curMode.periods.remove(operating.position);
				break;
			case 1:
				curMode.periods.add(operating.position, operating.value);
				break;
			case 2:
				curMode.periods.remove(operating.position);
				curMode.periods.add(operating.value);
				break;
			default:
				break;
			}
		}
		operatingSequence.clear();
	}
	
	private void ChangeModeListView(int spinnerSelection) {
		curSelectedModeId=modesIdVec.elementAt(spinnerSelection);				
		ReviewMode curMode=mApp.modes.get(curSelectedModeId);
		tv_modeName.setText(curMode.name);
		lv_peroids_adapter.SetData(curMode);
	}
	
	class ReviewModeListAdapter extends BaseAdapter{
		private List<Integer> curShowList=new ArrayList<Integer>();
		private ReviewMode data;
		
		public ReviewModeListAdapter() {  
        }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return curShowList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return curShowList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView==null) {
				String inflater=Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater layoutInflater=(LayoutInflater)activityContext.getSystemService(inflater);
				convertView=layoutInflater.inflate(R.layout.listview_mode_item,null);
				holder=new ViewHolder();
				holder.tv_peroidTime=(TextView)convertView.findViewById(R.id.tv_peroidTime);
				holder.tv_peroid=(TextView)convertView.findViewById(R.id.tv_peroid);
				holder.ibtn_editPeroid=(ImageButton)convertView.findViewById(R.id.ibtn_editPeroid);
				holder.ibtn_deletePeroid=(ImageButton)convertView.findViewById(R.id.ibtn_deletePeroid);
				convertView.setTag(holder);
			}else
				holder = (ViewHolder) convertView.getTag();
						
			holder.tv_peroidTime.setText(String.valueOf(position+1)); 
			Log.d("ReviewModeActivity-getView", ""+position);
			holder.tv_peroid.setText(curShowList.get(position).toString());
			
			holder.ibtn_editPeroid.setOnClickListener(new OnClickListener() {
				EditText et_peroid;
				Integer oldPeriod;
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int position = lv_periods.getPositionForView(v);
					if (position != ListView.INVALID_POSITION) {
			            //DO THE STUFF YOU WANT TO DO WITH THE position
						AlertDialog.Builder builder = new Builder(activityContext);
						builder.setTitle("���������ڼ��");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						et_peroid=new EditText(activityContext);
						oldPeriod=curShowList.get(position);
						et_peroid.setText(oldPeriod.toString());
						et_peroid.setKeyListener(new DigitsKeyListener(false,true));
						builder.setView(et_peroid);
						builder.setPositiveButton("ȷ��",new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Operating operating=new Operating(2,position,oldPeriod);
								operatingSequence.add(operating);
								String et_peroid_contant=et_peroid.getText().toString();
								curShowList.remove(position);
								curShowList.add(position, Integer.valueOf(Integer.parseInt(et_peroid_contant)));
								dialog.dismiss();
								notifyDataSetChanged();
							}

						});
						builder.setNegativeButton("ȡ��",new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}

						});						
						builder.create().show();
			        }
				}
			});
			
			holder.ibtn_deletePeroid.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int position = lv_periods.getPositionForView(v);
					if (position != ListView.INVALID_POSITION) {
			            //DO THE STUFF YOU WANT TO DO WITH THE position
						Operating operating=new Operating(0,position,curShowList.get(position));
						operatingSequence.add(operating);
						RemovePeriod(position);
			        }
				}
			});
						
			return convertView;
		}
		
		public void SetData(ReviewMode data) {
			this.data=data;
			curShowList.clear();
			curShowList.addAll(this.data.periods);
			notifyDataSetChanged();
		}
		
		
		public void OnResetData() {
			curShowList.clear();
			curShowList.addAll(this.data.periods);
			operatingSequence.clear();
			notifyDataSetChanged();
		}
		
		public void AddNewPeriod(int position,Integer newPeriod) {
			curShowList.add(position, newPeriod);
			notifyDataSetChanged();
		}
		
		public void RemovePeriod(int position) {
			curShowList.remove(position);
			notifyDataSetChanged();
		}
		
		public void SetPeriod(int position,Integer period) {
			curShowList.remove(position);
			curShowList.add(position,period);
			notifyDataSetChanged();
		}
		
	}
	
	static class ViewHolder{
		TextView tv_peroidTime;
		TextView tv_peroid;
		ImageButton ibtn_editPeroid;
		ImageButton ibtn_deletePeroid;
	}
	
	static class Operating{
		
		Operating(int operate,int position){
			this.operate=operate;
			this.position=position;
		}
		
		Operating(int operate,int position,Integer value){
			this.operate=operate;
			this.position=position;
			this.value=value;
		}
		int operate;//0-ɾ����1-���ӣ�2-�޸�
		int position;//lv_mode�����ڵ�λ��
		Integer value;//��operateΪ0��2ʱʹ��
	}
}