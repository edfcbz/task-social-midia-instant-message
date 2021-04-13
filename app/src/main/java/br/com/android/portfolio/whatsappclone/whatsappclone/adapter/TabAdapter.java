package br.com.android.portfolio.whatsappclone.whatsappclone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.android.portfolio.whatsappclone.whatsappclone.fragment.ContactFragment;
import br.com.android.portfolio.whatsappclone.whatsappclone.fragment.ConversationFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"CONVERSAS","CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ConversationFragment();
                break;

            case 1:
                fragment = new ContactFragment();
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position)  {
        return tituloAbas[position];
    }

}
