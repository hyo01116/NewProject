package com.example.newproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.newproject.Activity.GeneralUserActivity;
import com.example.newproject.Activity.LoginActivity;
import com.example.newproject.Class.GeneralUserInfo;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.UserResponse;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.helper.log.Logger;

public class KakaoLoginActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        Log.i("KAKAO_API", "사용자 아이디: " + result.getId());
                        UserAccount kakaoAccount = result.getKakaoAccount();
                        if (kakaoAccount != null) {
                            String email = kakaoAccount.getEmail();
                            if (email != null) {
                                Log.i("KAKAO_API", "email: " + email);
                            } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                // 동의 요청 후 이메일 획득 가능
                                // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                            } else {
                                // 이메일 획득 불가
                            }
                            // 프로필
                            Profile profile = kakaoAccount.getProfile();
                            if (profile != null) {
                                Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                                Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                                Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());

                            } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                // 동의 요청 후 프로필 정보 획득 가능

                            } else {
                                // 프로필 획득 불가
                            }
                            startActivity(GeneralUserActivity.class);
                        }
                    }
                });
    }
    public void startActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    public void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
