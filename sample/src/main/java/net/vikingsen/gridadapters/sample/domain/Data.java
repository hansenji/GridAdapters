package net.vikingsen.gridadapters.sample.domain;

import net.vikingsen.gridadapters.sample.R;

/**
 * Created by Jordan Hansen
 */
public class Data {

    public static int getImageId(String distro) {
        switch (distro) {
            case "AMLUG":
                return R.drawable.amlug;
            case "ARCHLINUX":
                return R.drawable.archlinux;
            case "ASPLINUX":
                return R.drawable.asplinux;
            case "BEAR OPS":
                return R.drawable.bearops;
            case "BEATRIX":
                return R.drawable.beatrix;
            case "BIGLINUX":
                return R.drawable.biglinux;
            case "BLAG":
                return R.drawable.blag;
            case "CENTOS":
                return R.drawable.centos;
            case "CHINESE LINUX EXTENSION":
                return R.drawable.chineselinuxextension;
            case "COREL LINUX":
                return R.drawable.corellinux;
            case "DEBIAN":
                return R.drawable.debian;
            case "DELI LINUX":
                return R.drawable.delilinux;
            case "ENGARDE":
                return R.drawable.engarde;
            case "EVILENTITY":
                return R.drawable.evilentity;
            case "FEDORA":
                return R.drawable.fedora;
            case "FORESIGHT":
                return R.drawable.foresight;
            case "FREEDUC":
                return R.drawable.freeduc;
            case "FRUGALWARE":
                return R.drawable.frugalware;
            case "GEEKBOX":
                return R.drawable.geekbox;
            case "GENTOO LINUX":
                return R.drawable.gentoolinux;
            case "HAYDAR LINUX":
                return R.drawable.haydarlinux;
            case "HIWEEK":
                return R.drawable.hiweed;
            case "JAMD LINUX":
                return R.drawable.jamdlinux;
            case "JULEX":
                return R.drawable.julex;
            case "KALANGO":
                return R.drawable.kalango;
            case "KALI LINUX":
                return R.drawable.kalilinux;
            case "KNOPPIX STD":
                return R.drawable.knoppixstd;
            case "LINSPIRE":
                return R.drawable.linspire;
            case "LPG":
                return R.drawable.lpg;
            case "LUNAR":
                return R.drawable.lunar;
            case "LYCORIS":
                return R.drawable.lycoris;
            case "MANDRAKELINUX":
                return R.drawable.mandrakelinux;
            case "MEPIS":
                return R.drawable.mepis;
            case "MINIKAZIT":
                return R.drawable.minikazit;
            case "MINT":
                return R.drawable.mint;
            case "MONOWALL":
                return R.drawable.monowall;
            case "PCLINUXOS":
                return R.drawable.pclinuxos;
            case "PLD":
                return R.drawable.pld;
            case "PROGENY":
                return R.drawable.progeny;
            case "REDHAT":
                return R.drawable.redhat;
            case "SABAYON":
                return R.drawable.sabayon;
            case "SLACKINTOSH":
                return R.drawable.slackintosh;
            case "SLACKWARE LINUX":
                return R.drawable.slackwarelinux;
            case "SLAX":
                return R.drawable.slax;
            case "SUSE":
                return R.drawable.suse;
            case "TURBOLINUX":
                return R.drawable.turbolinux;
            case "UBUNTU":
                return R.drawable.ubuntu;
            case "UTUTO":
                return R.drawable.ututo;
            case "VINE LINUX":
                return R.drawable.vinelinux;
            case "YELLOW DOG LINUX":
                return R.drawable.yellowdoglinux;
            case "YOPER":
                return R.drawable.yoper;
            default:
                throw new IllegalStateException("Invalid distro name: " + distro);
        }
    }
}
