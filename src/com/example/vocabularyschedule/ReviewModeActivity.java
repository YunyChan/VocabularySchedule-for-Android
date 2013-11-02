package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Enumeration;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewModeActivity extends Activity{	
	ReviewModeActivity activity=this;
	private MainApp mApp;
	
	TextView tv_modeName;
	ImageButton ibtn_deleteMode,ibtn_addMode,ibtn_editModeName;
	Button btn_reset,btn_addPeriod,btn_save,btn_undo;
	
	//
	ListView lv_periods;
	ReviewModeListAdapter lv_peroids_adapter;

	
	//spn_books所使用的变量
	Spinner spn_modes;
	ArrayAdapter<String> spn_modes_adapter;
	Integer curSelectedModeId;
	
	//-2代表新增,-1代表删除，>0代表修改
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
		InitBtnUndo();
		InitBtnReset();
		InitBtnAddPeriod();
		InitBtnSave();
	}
	
	private void InitModesSpinner(){
		spn_modes=(Spinner)findViewById(R.id.spn_modes);
		mApp.ResetModesSpinnerData();
		SetSpinnerAdapter();
		spn_modes.setOnItemSelectedListener(new OnItemSelectedListener() {
			int ItemSelectedPosition;
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				ItemSelectedPosition=arg2;
				if(operatingSequence.size()>0){
					AlertDialog.Builder builder = new Builder(activity);
					builder.setTitle("还没保存操作！");
					builder.setIcon(android.R.drawable.ic_dialog_info);
					TextView tv_saveWarning=new TextView(activity);
					tv_saveWarning.setText("确定不保存对本模式的所有操作？");
					tv_saveWarning.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					builder.setView(tv_saveWarning);
					builder.setPositiveButton("不保存", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							operatingSequence.clear();
							ChangeModeListView(ItemSelectedPosition);
							arg0.dismiss();
						}
						
					});
					builder.setNegativeButton("保存", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SetAllOperatings();
							ChangeModeListView(ItemSelectedPosition);
							dialog.dismiss();
						}
						
					});
					builder.create().show();
				}else
					ChangeModeListView(ItemSelectedPosition);
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
						ReviewMode newMode=new ReviewMode(new Vector<Integer>(),"新复习模式",newId);
						spn_modes_adapter.add(newMode.name);
						int newModeSpinnerIndex=spn_modes_adapter.getCount()-1;
						spn_modes.setSelection(newModeSpinnerIndex);
						curSelectedModeId=Integer.valueOf(newId);
						mApp.modes.put(curSelectedModeId, newMode);
						mApp.modesIdVec.add(curSelectedModeId);
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
							mApp.ResetModesSpinnerData();
							SetSpinnerAdapter();
						}else
							Toast.makeText(getApplicationContext(),"还有"+totalSelectedBooks+"本书占用该模式",Toast.LENGTH_SHORT).show();
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
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("模式的名字");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_modeName=new EditText(activity);
				et_modeName.setText(tv_modeName.getText());
				builder.setView(et_modeName);
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String newModeName=et_modeName.getText().toString();
						mApp.modes.get(curSelectedModeId).name=newModeName;
						tv_modeName.setText(newModeName);
						int curSpinnerIndex=spn_modes.getSelectedItemPosition();
						spn_modes_adapter.clear();
						mApp.ResetModesSpinnerData();
						SetSpinnerAdapter();
						spn_modes.setSelection(curSpinnerIndex);
						arg0.dismiss();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

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
	
	private void InitBtnUndo() {
		
		btn_undo=(Button)findViewById(R.id.btn_undo);
		btn_undo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(operatingSequence.size()>0)
					CancelLastOperating();
			}
		});
	}
	
	private void InitBtnReset() {
		btn_reset=(Button)findViewById(R.id.btn_reset);
		btn_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				while(operatingSequence.size()>0)
					CancelLastOperating();
			}
		});
	}
	
	private void InitBtnAddPeriod() {
		btn_addPeriod=(Button)findViewById(R.id.btn_addPeriod);
		btn_addPeriod.setOnClickListener(new OnClickListener() {
			EditText et_period;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("第"+(lv_peroids_adapter.getCount()+1)+"周期的间隔天数");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_period=new EditText(activity);
				et_period.setHint("请输入天数");
				et_period.setKeyListener(new DigitsKeyListener(false,true));
				builder.setView(et_period);
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Operating operating=new Operating(1,lv_peroids_adapter.getCount());
						operatingSequence.add(operating);
						String et_period_contant=et_period.getText().toString();
						Integer value=Integer.valueOf(Integer.parseInt(et_period_contant));
						operating.value=value;
						lv_peroids_adapter.AddNewPeriod(lv_peroids_adapter.getCount(),value);
						arg0.dismiss();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

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
	
	private void InitBtnSave() {
		btn_save=(Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetAllOperatings();
			}
		});
	}
	
	private void CancelLastOperating() {
		Operating lastOperating=operatingSequence.lastElement();
		switch (lastOperating.operate) {
		case 0://删除
			lv_peroids_adapter.AddNewPeriod(lastOperating.position, lastOperating.value);
			break;
		case 1://添加
			lv_peroids_adapter.RemovePeriod(lastOperating.position);
			break;
		case 2://修改
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
				curMode.periods.add(operating.value);
				break;
			case 2:
				curMode.periods.remove(operating.position);
				curMode.periods.add(operating.position,operating.value);
				break;
			default:
				break;
			}
		}
		operatingSequence.clear();
	}
	
	private void ChangeModeListView(int spinnerSelection) {
		curSelectedModeId=mApp.modesIdVec.elementAt(spinnerSelection);				
		ReviewMode curMode=mApp.modes.get(curSelectedModeId);
		tv_modeName.setText(curMode.name);
		lv_peroids_adapter.SetData(curMode);
	}
	
	private void SetSpinnerAdapter() {
		spn_modes_adapter=new
				ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,mApp.spn_modes_al);
		spn_modes_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_modes.setAdapter(spn_modes_adapter);
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
				LayoutInflater layoutInflater=(LayoutInflater)activity.getSystemService(inflater);
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
						AlertDialog.Builder builder = new Builder(activity);
						builder.setTitle("请输入周期间隔");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						et_peroid=new EditText(activity);
						oldPeriod=curShowList.get(position);
						et_peroid.setText(oldPeriod.toString());
						et_peroid.setKeyListener(new DigitsKeyListener(false,true));
						builder.setView(et_peroid);
						builder.setPositiveButton("确定",new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Operating operating=new Operating(2,position,oldPeriod);
								operatingSequence.add(operating);
								String et_peroid_contant=et_peroid.getText().toString();
								Integer value=Integer.valueOf(Integer.parseInt(et_peroid_contant));
								operating.value=value;
								SetPeriod(position,value);
								dialog.dismiss();
								notifyDataSetChanged();
							}

						});
						builder.setNegativeButton("取消",new android.content.DialogInterface.OnClickListener() {
							
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
		int operate;//0-删除，1-增加，2-修改
		int position;//lv_mode中周期的位置
		Integer value;//当operate为0和2时使用
	}
}
