package com.example.jh.raiseplant.SelfCheck_Code;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jh.raiseplant.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SelfCheckFragment  extends Fragment {

    ListView m_ListView;
    CustomAdapter m_Adapter;

    String date_text = new SimpleDateFormat("EE요일, MM월 dd일 ", Locale.getDefault()).format(Calendar.getInstance().getTime());
    String date_time = new SimpleDateFormat("HH : mm  aa", Locale.US).format(Calendar.getInstance().getTime());

    private int i;

    //질문 목록
    private String[] Dr_Plant = {
            "안녕하세요. 자가진단 하실 식물의 이름을 알려주세요.",
            "잎 표면이 갈색으로 변했나요?", //1
            "잎 또는 줄기가 쳐져있나요?", //2
            "악취가 나나요?",             //3
            "잎 표면에 여러 검은점이 생겼나요? ", //4
            "먼지가 쌓인듯한 곰팡이가 생겼나요? ", //5
            "줄기가 검게 변했나요?",//6
            "잎이 떨어졌나요?",//7
            "자가진단 질문이 모두 종료되었습니다. 확인해보시겠습니까?",//8
            "진단하실 식물이 더 있으신가요?"//9
    };

    //대답 저장
    final String[] answer = new String[Dr_Plant.length];

    //식물 목록
    private String[] plantName = {
            "콩이", "버터커피", "석탄", "zz"
    };

    //병명과 증상, 진단내용
    PlantDisease[] plantDisease = {
            new PlantDisease("잿빛 곰팡이병"
                    , "주로 잎과 줄기에 침입하여 피해를 주며, 잎줄기에 암자색의 부정형으로 부패되고 움푹 패이거나 잘록해지며 병이 심해지면 잎줄기가 시들어 고사합니다. 습도가 높고 저온일 때 병반에서 먼지 같은 곰팡이 포자를 볼 수 있으며, 심하면 전체가 갈변하여 죽고 그 부위에 잿빛곰팡이가 많이 발생합니다.\n"
                    , "외부 온도가 낮고 시설내부의 온도가 높을 때 발생하며, 공기에 의해 전염됩니다. 특히 겨울이나 봄의 저온, 다습조건에서 많이 발생합니다다"),
            new PlantDisease("탄저병",
                    "처음에는 잎에 타원형의 담황색 점무늬가 생기고 후에 그 주위가 흑갈색으로 변합니다. ",
                    "조직의 일부가 죽어도 병반은 살아있어 포자를 형성하며 건전한 식물체로 전염됩니다. 고온 다습기에 많이 발생하므로, 가능한 식물체가 젖어있지 않도록 관리하여야 하는데, 환풍기를 설치하여 시설 내 온도와 습도를 낮추도록 노력해야 합니다. "),
            new PlantDisease("웃자 현상",
                    "햇빛이 부족하거나 토양의 습도가 높아 생기는 증상으로 이미 웃자란 식물은 되돌리기 어렵습니다.",
                    "밝은 베란다와 같은 곳에서 키우고, 화분은 토분이나 물빠짐이 잘 되는 것으로 바꿔주시길 바랍니다. 물은 흙이 충분히 마른 후에 주시면서 낮에는 따뜻하고, 밤에는 10℃ 내외로 선선하게 관리하시길 바랍니다."),
            new PlantDisease("무름병",
                    "잎과 줄기에 수침상 부패 병반이 생겨 점차 확대되어 잎과 줄기가 연화합니다. 심할 경우 잎과 뿌리로 연화가 급속히 진전되며, 전체가 갈색 또는 흑갈색으로 변하고 결국 시들어 물러 썩으며 심한 악취가 나기도 합니다.",
                    "토양해충이 식물체를 가해할 때나 기타 상처를 통해 식물체 내에 침입하기도 하며 감염 부위의 상처를 통하여 곰팡이의 감염이 일어나기도 합니다. 특히 기온과 지온이 높고 다습한 환경에서 발병이 많습니다."),
            new PlantDisease("검은 점무늬병",
                    "\"잎 전체에 직경 1∼2㎜의 둥글고 작은 점무늬를 형성하며, 주로 잎에 발생합니다. 초기에는 흑갈색의 작은 점무늬의 병반을 형성하고, 이 병반은 점차 확대되어 원형상으로 됩니다. ",
                    "잎이 물에 젖어 있거나 봄철 다습하고 환기가 잘되지 않을 경우 발생이 많아집니다. 환기를 잘 시켜 습도를 낮게 관리하며, 병든 식물체는 제거하고 불로 태우는게 좋습니다. "),
            new PlantDisease("지하부 줄기썩음병",
                    "줄기와 뿌리에 발병하며, 감염된 식물은 줄기의 지제부나 토양 접촉부분에 수침상의 검은 무늬가 생기고 점차 지상부로 퍼져 줄기 전체가 검게 변색되어 잎이 모두 떨어집니다.",
                    "병원균은 사상균이며 고온다습한 환경에서 발병이 특히 심합니다. 선인장·다육식물 재배 농가에서 연중 발생하며, 토양전염하고 삽목전염이나 분생포자의 비산에 의해서도 감염됩니다. 발병한 포기와 함께 주변의 토양도 가능한 빨리 제거하고 약제를 살포해야 합니다. "),
            new PlantDisease("역병",
                    "대부분의 선인장에서 발생되며 지제부의 줄기가 검게 변하고줄기가 급격히 시듭니다. 병이 진전되면 식물체 전체가 마르기도 하고 수침상으로 검게 썩으며 급격히 시들고 포기전체가 말라죽는 병징을 보입니다. 특정한 병징이 없다가도 하루 이틀 사이에 급격히 병이퍼지는 것 또한 역병의 특징입니다.",
                    "물을 좋아하는 수생성 곰팡이균에 의한 병이기 때문에 저면관수 시설에서 역병에 감염되면 더욱 피해가 커지는 특징을 나타냅니다. 어린 묘에 발생하면 병이 급속히 진전되어 전멸하기도 합니다. 병원균이 토양과 물에 의해 이동한 후 인근 식물체로 전염이 가능하므로 병든 식물체는 즉시 제거합니다. "
            )
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup chatView = (ViewGroup) inflater.inflate(R.layout.fragment_selfcheck, container, false);

        // 커스텀 어댑터 생성
        m_Adapter = new CustomAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) chatView.findViewById(R.id.chatlist);
        m_ListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        //챗봇 처음 시간
        m_Adapter.add(date_text, date_time, 2);
        date_time = new SimpleDateFormat("HH : mm  aa", Locale.US).format(Calendar.getInstance().getTime());
        m_Adapter.add(Dr_Plant[i], date_time, 0);

        final EditText editText = (EditText) chatView.findViewById(R.id.chatEdit);


        chatView.findViewById(R.id.chatSend).setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        date_time = new SimpleDateFormat("HH : mm  aa", Locale.US).format(Calendar.getInstance().getTime());
                        String inputValue = editText.getText().toString();
                        editText.setText(null);

                        refresh(inputValue, date_time, 1);
                        if (i == 0) {
                            //식물이름 물어보는거
                            int count = 0;
                            for (String plNa : plantName) {
                                if (inputValue.matches(".*" + plNa + ".*")) {
                                    answer[i++] = inputValue;
                                    refresh(Dr_Plant[i], date_time, 0);
                                    break;
                                } else count++;
                            }
                            if (count != 0 && count == plantName.length) {
                                refresh("없는식물입니다. 다시 입력해주세요.", date_time, 0);
                                i = 0;
                            }
                        } else if (0 < i && i < Dr_Plant.length - 2) {
                            if (anwMatches(inputValue) == 10) {
                                refresh("네 또는 아니오 로 대답해주세요!", date_time, 0);
                            } else {
                                if (anwMatches(inputValue) == 1) answer[i] = "네";
                                else answer[i] = "아니오";
                                i++;
                                refresh(Dr_Plant[i], date_time, 0);
                            }
                        } else {
                            int count = 0;
                            for (int c = 1; c < plantDisease.length; c++) {
                                count++;
                                if (plantDisease[c].getName().equals(diagnosis(answer))) {
                                    refresh("해당 증상은 " + plantDisease[c].getName() + "으로 진단됩니다. \n\n" +
                                            plantDisease[c].getDiagnosis() + "\n\n" + plantDisease[c].getMethod(), date_time, 0);
                                    break;
                                } else if (count == plantDisease.length-1){
                                    refresh("죄송합니다. 증상을 알 수 없습니다. 전문가 진단을 이용해주세요.", date_time, 0);
                                }
                            }
                            i = 0;
                            refresh("다시 자가진단을 시작하려면 식물의 이름을 알려주세요.", date_time, 0);
                        }
                    }
                }
        );
        return chatView;
    }

    private void refresh(String inputValue, String date_time, int _str) {
        m_Adapter.add(inputValue, date_time, _str);
        m_Adapter.notifyDataSetChanged();
    }

    public int anwMatches(String inputValue) {
        String[] yes = {".*네.*", ".*넵.*", ".*예.*", ".*ㅇ.*", ".*yes.*", ".*넹.*"};
        String[] no = {".*아니.*", ".*no.*", ".*ㄴ.*", ".*아녀.*", ".*아뇨.*"};
        for (String y : yes) if (inputValue.matches(y)) return 1;
        for (String n : no) if (inputValue.matches(n)) return 0;
        return 10;
    }

    public String diagnosis(String[] a) {
        if (a[1] == a[5] && a[1].equals("네")) return "잿빛 곰팡이병";
        else if (a[1] == a[4] && a[1].equals("네")) return "탄저병";
        else if (a[1] == a[2] && a[1].equals("네")) return "웃자 현상";
        else if (a[1] == a[2] && a[1] == a[3] && a[1].equals("네")) return "무름병";
        else if (a[4].equals("네")) return "검은 점무늬병";
        else if (a[6] == a[7] && a[6].equals("네")) return "지하부 줄기썩음병";
        else if (a[6].equals("네")) return "역병";
        else return "알 수 없음.";
    }

}

    class PlantDisease {
        private String name;
        private String diagnosis ;
        private String method ;

        public PlantDisease(String name, String diagnosis, String method) {
            this.name = name;
            this.diagnosis = diagnosis;
            this.method = method;
        }

        public String getName() {
            return name;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public String getMethod() {
            return method;
        }

    }