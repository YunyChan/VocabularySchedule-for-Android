package com.example.vocabularyschedule;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	private Context context;
	private List<TableRow> table; 
	private View[] viewArray;
	
	public ListViewAdapter(Context context, List<TableRow> table) {  
        this.context = context;  
        this.table = table; 
        viewArray = new View[table.size()];
    }  

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return table.size(); 
	}

	@Override
	public TableRow getItem(int arg0) {
		// TODO Auto-generated method stub
		return table.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (viewArray[position]==null){
        	TableRow tableRow = table.get(position); 
        	viewArray[position] = new TableRowView(this.context, tableRow); 
        }
        return viewArray[position];
	}
	
    /** 
     * TableRowView ʵ�ֱ���е���ʽ 
     * @author hellogv 
     */  
    class TableRowView extends LinearLayout {  
        public TableRowView(Context context, TableRow tableRow) {  
            super(context);  
              
            this.setOrientation(LinearLayout.HORIZONTAL);  
            for (int i = 0; i < tableRow.getSize(); i++) {//�����Ԫ��ӵ���  
                TableCell tableCell = tableRow.getCellValue(i);  
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  
                        tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�  
                layoutParams.setMargins(0, 0, 1, 1);//Ԥ����϶����߿�  
                if (tableCell.type == TableCell.STRING) {//�����Ԫ���ı�����  
                    TextView textCell = new TextView(context);  
                    textCell.setLines(1);  
                    textCell.setGravity(Gravity.CENTER);  
                    textCell.setBackgroundColor(Color.WHITE);//������
                    textCell.setText(String.valueOf(tableCell.value));  
                    addView(textCell, layoutParams);  
                } else if (tableCell.type == TableCell.IMAGE) {//�����Ԫ��ͼ������  
                    ImageView imgCell = new ImageView(context);  
                    imgCell.setBackgroundColor(Color.WHITE);//������ɫ  
                    imgCell.setImageResource((Integer) tableCell.value);  
                    addView(imgCell, layoutParams);  
                }  else if (tableCell.type == TableCell.CHECKBOX) {
                	CheckBox checkBoxCell=new CheckBox(context);
                	checkBoxCell.setBackgroundColor(Color.WHITE);
                	checkBoxCell.setChecked((Boolean)tableCell.value);
                	addView(checkBoxCell, layoutParams);
				}else if (tableCell.type == TableCell.ITEM) {
					LinearLayout item=new LinearLayout(context);
					item.setOrientation(LinearLayout.VERTICAL);					
					LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(  
	                        tableCell.width, 25);

					TextView textCell = new TextView(context);
					textCell.setLines(1);
					textCell.setGravity(Gravity.LEFT);
					textCell.setBackgroundColor(Color.WHITE);//������
					textCell.setText(String.valueOf(1));
					item.addView(textCell, itemParams);
					
					itemParams = new LinearLayout.LayoutParams(  
	                        tableCell.width, 20);
					textCell = new TextView(context);
					textCell.setLines(1);
					textCell.setGravity(Gravity.CENTER);
					textCell.setBackgroundColor(Color.WHITE);//������
					textCell.setText("~");
					item.addView(textCell, itemParams);
					
					itemParams = new LinearLayout.LayoutParams(  
	                        tableCell.width, 25);
					textCell = new TextView(context);
					textCell.setLines(1);
					textCell.setGravity(Gravity.RIGHT);
					textCell.setBackgroundColor(Color.WHITE);//������
					textCell.setText(String.valueOf(30));
					item.addView(textCell, itemParams);
					
					
					this.addView(item,layoutParams);
				}
            }  
            this.setBackgroundColor(Color.BLACK);//������ɫ�����ÿ�϶��ʵ�ֱ߿�              
        }  
    }  
    /** 
     * TableRow ʵ�ֱ����� 
     * @author hellogv 
     */  
    static public class TableRow {  
        private TableCell[] cell;  
        public TableRow(TableCell[] cell) {  
            this.cell = cell;  
        }  
        public int getSize() {  
            return cell.length;  
        }  
        public TableCell getCellValue(int index) {  
            if (index >= cell.length)  
                return null;  
            return cell[index];  
        }  
    }  
    /** 
     * TableCell ʵ�ֱ��ĸ�Ԫ 
     * @author hellogv 
     */  
    static public class TableCell {  
        static public final int STRING = 0;  
        static public final int IMAGE = 1;  
        static public final int CHECKBOX = 2; 
        static public final int ITEM = 3; 
        public Object value;  
        public int width;  
        public int height;  
        private int type;  
        public TableCell(Object value, int width, int height, int type) {  
            this.value = value;  
            this.width = width;  
            this.height = height;  
            this.type = type;  
        }  
    }  

}

