package com.punuo.pet.member;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.punuo.pet.member.request.LogoutRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.DataClearUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 我的页面
 **/
@Route(path = MemberRouter.ROUTER_MEMBER_FRAGMENT)
public class MemberFragment extends BaseFragment implements View.OnClickListener{

    private ImageView mBack;
    private TextView mExitButton;
    private TextView mAddPetButton;
    private TextView mNickname;
    private ImageView mAvater;
    private TextView mBuff;
    private RelativeLayout mAccount;
    private RelativeLayout mCache;
    private  RelativeLayout mPrivacy;
    private RelativeLayout mAbout;
    private RelativeLayout mCustomer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_member, container, false);

        initView();

        View statusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            statusBar.setVisibility(View.VISIBLE);
            statusBar.requestLayout();
        }

        return mFragmentView;
    }

    private void initView() {

        mExitButton = mFragmentView.findViewById(R.id.exit_button);
        mAddPetButton = mFragmentView.findViewById(R.id.add_pet_button);
        mNickname = mFragmentView.findViewById(R.id.user_nickname);
        mAvater = mFragmentView.findViewById(R.id.user_avater);
        mAccount = mFragmentView.findViewById(R.id.account);
        mBuff = mFragmentView.findViewById(R.id.buff);
        mCache = mFragmentView.findViewById(R.id.cache);
        mPrivacy = mFragmentView.findViewById(R.id.privacy);
        mAbout = mFragmentView.findViewById(R.id.about);
        mCustomer = mFragmentView.findViewById(R.id.customerservice);

        mBuff.setText(DataClearUtil.getTotalCacheSize(getActivity()));

        mExitButton.setOnClickListener(this);
        mAddPetButton.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mCache.setOnClickListener(this);
        mPrivacy.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mCustomer.setOnClickListener(this);



        //设置用户头像
        String avater = AccountManager.getUserInfo().avatar;
        Glide.with(getActivity()).load(avater).into(mAvater);

        //设置用户昵称
        String nickname = AccountManager.getUserInfo().nickName;
        mNickname.setText(nickname);

        //设置用户ID
    }




    private LogoutRequest mLogoutRequest;
    public void logout(String userName) {
        if (mLogoutRequest != null && !mLogoutRequest.isFinish()) {
            return;
        }
        showLoadingDialog("正在退出...");
        mLogoutRequest = new LogoutRequest();
        mLogoutRequest.addUrlParam("userName", userName);
        mLogoutRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    AccountManager.clearAccountData();
                    ARouter.getInstance().build(MemberRouter.ROUTER_LOGIN_ACTIVITY).navigation();
                    getActivity().finish();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mLogoutRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.exit_button){
            logout(AccountManager.getSession());
        } else if(id == R.id.add_pet_button){
            ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY).navigation();
        } else if(id == R.id.account){
            ARouter.getInstance().build(MemberRouter.ROUTER_ACCOUNT_MANAGEMENT_ACTIVITY).navigation();
        } else if(id==R.id.privacy){
            ARouter.getInstance().build(MemberRouter.ROUTER_PRIVACY_ACTIVITY).navigation();
        } else if(id==R.id.cache){
            DataClearUtil.cleanAllCache(getActivity());
            mBuff.setText(DataClearUtil.getTotalCacheSize(getActivity()));
        }else if(id==R.id.about){
            ARouter.getInstance().build(MemberRouter.ROUTER_ABOUT_ACTIVITY).navigation();
        }else if(id == R.id.customerservice){
            ARouter.getInstance().build(MemberRouter.ROUTER_CUETOMERSERVICE).navigation();
        }

    }
}
