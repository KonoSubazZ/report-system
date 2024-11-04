package mutation_explanation;
use warnings;
use strict;

=head1 NAME

	mutation_explanation - perl module for nucleotide and amino acid Chinese explanation
	Author:		Gao Changxin (gaochangxin@novogene.com)
	Version:	2.0
	Date:		v1.0	2015-11-30
			v1.0.1	2016-02-29	Add the aa ins
			v1.0.2	2016-04-13	Add the aa del
			v2.0	2018-02-07	For hgvs standard description
			v2.1	2019-08-30	Add promoter and dup ##RD

=head1 SYNOPSIS

	use mutation_explanation;
	my $mut=mutation_explanation->new();
	my $chgvs=$mut->chgvs(c.***);	#返回碱基变异描述，支持格式为c.44A>C
	my $phgvs=$mut->phgvs(p.***);	#返回氨基酸变异描述，支持格式为p.Ala222Val或p.A222V

=cut

sub new {
	my $class=shift;
	my %opts=@_;
	my $self={%opts};
	bless $self, ref($class) || $class;
	return $self;
}

sub chgvs {
	my ($self,$exon,$chgvs)=@_;
	my @region=split(/_/,$exon);
	my ($exon1,$exon2)=("","");
	$exon1=$self->exon($region[0]);
	$exon2=$self->exon($region[1]) if $region[1];
	if ($chgvs=~/c\.([\d\+\-\*]+)([ACGT])>([ACGT]+)$/) {	#SNP
		return "位于".$exon1."的".$self->cpos($1)."核苷酸$2被核苷酸$3替代";
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)_([\d\+\-\*]+)([ACGT]+)>([ACGT]+)$/) { #MNP
		return "位于".$exon1."的".$self->cpos($1) . "到".$self->cpos($2)."核苷酸$3被核苷酸$4替代";
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)_([\d\+\-\*]+)ins([ACGTN]+)$/) {	#ins
		if ($exon2) {
			return "位于".$exon1.$self->cpos($1)."与".$exon2.$self->cpos($2)."之间插入核苷酸".$3;
		} else {
			return "位于".$exon1."的".$self->cpos($1)."与".$self->cpos($2)."之间插入核苷酸".$3;
		}
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)dup([ACGTN]?)$/) {	#dup 1bp
		return "位于".$exon1."的".$self->cpos($1)."核苷酸".$2."发生重复";
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)_([\d\+\-\*]+)dup([ACGTN]*)$/) {	#dup >1bp
		if ($exon2) {
			return "位于".$exon1.$self->cpos($1)."到".$exon2.$self->cpos($2)."核苷酸".$3."发生重复";
		} else {
			return "位于".$exon1."的".$self->cpos($1)."到".$self->cpos($2)."核苷酸".$3."发生重复";
		}
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)del([ACGTN]?)$/) {	#del 1bp
		return "位于".$exon1."的".$self->cpos($1)."缺失核苷酸".$2;
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)_([\d\+\-\*]+)del([ACGTN]*)$/) {	#del >1bp
		if ($exon2) {
			return "位于".$exon1.$self->cpos($1)."到".$exon2.$self->cpos($2)."缺失核苷酸".$3;
		} else {
			return "位于".$exon1."的".$self->cpos($1)."到".$self->cpos($2)."缺失核苷酸".$3;
		}
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)del([ACGTN]?)ins([ACGTN]+)$/) {	#delins one to more
		return "位于".$exon1."的".$self->cpos($1)."缺失核苷酸".$2."并插入核苷酸".$3;
	} elsif ($chgvs=~/c\.([\d\+\-\*]+)_([\d\+\-\*]+)del([ACGTN]*)ins([ACGTN]+)$/) {	#delins more to more
		if ($exon2) {
			return "位于".$exon1.$self->cpos($1)."到".$exon2.$self->cpos($2)."缺失核苷酸".$3."并插入核苷酸".$4;
		} else {
			return "位于".$exon1."的".$self->cpos($1)."到".$self->cpos($2)."缺失核苷酸".$3."并插入核苷酸".$4;
		}
	}
}

sub exon {
	my ($self,$exon)=@_;
	if ($exon=~/exon(\d+)/) {
		return "$1号外显子上";
	} elsif ($exon=~/intron(\d+)/) {
		return "$1号内含子上";
	} elsif ($exon=~/promoter/) {
        return "启动子区";
    }
}

sub cpos {
	my ($self,$pos)=@_;
	my $s = "";
	if ($pos=~/\*/) {
		$s = "翻译终止密码子下游";
	}
	if ($pos=~/(\d+)-(\d+)/) {
		return $s.="-$2位";
	} elsif ($pos=~/(\d+)\+(\d+)/) {
		return $s.="+$2位";
	} else {
		return $s.="第$pos位";
	}
}

sub phgvs {
	my ($self,$phgvs)=@_;
	my %aa_exp=(	
		"Ala"	=>	"丙氨酸(Ala)",	"A"	=>	"丙氨酸(A)",
		"Arg"	=>	"精氨酸(Arg)",	"R"	=>	"精氨酸(R)",
		"Glu"	=>	"谷氨酸(Glu)",	"E"	=>	"谷氨酸(E)",
		"His"	=>	"组氨酸(His)",	"H"	=>	"组氨酸(H)",
		"Gly"	=>	"甘氨酸(Gly)",	"G"	=>	"甘氨酸(G)",
		"Leu"	=>	"亮氨酸(Leu)",	"L"	=>	"亮氨酸(L)",
		"Lys"	=>	"赖氨酸(Lys)",	"K"	=>	"赖氨酸(K)",
		"Pro"	=>	"脯氨酸(Pro)",	"P"	=>	"脯氨酸(P)",
		"Ser"	=>	"丝氨酸(Ser)",	"S"	=>	"丝氨酸(S)",
		"Thr"	=>	"苏氨酸(Thr)",	"T"	=>	"苏氨酸(T)",
		"Trp"	=>	"色氨酸(Trp)",	"W"	=>	"色氨酸(W)",
		"Tyr"	=>	"酪氨酸(Tyr)",	"Y"	=>	"酪氨酸(Y)",
		"Val"	=>	"缬氨酸(Val)",	"V"	=>	"缬氨酸(V)",
		"Asp"	=>	"天冬氨酸(Asp)",	"D"	=>	"天冬氨酸(D)",
		"Cys"	=>	"半胱氨酸(Cys)",	"C"	=>	"半胱氨酸(C)",
		"Gln"	=>	"谷氨酰胺(Gln)",	"Q"	=>	"谷氨酰胺(Q)",
		"Met"	=>	"甲硫氨酸(Met)",	"M"	=>	"甲硫氨酸(M)",
		"Phe"	=>	"苯丙氨酸(Phe)",	"F"	=>	"苯丙氨酸(F)",
		"Ile"	=>	"异亮氨酸(Ile)",	"I"	=>	"异亮氨酸(I)",
		"Asn"	=>	"天冬酰胺(Asn)",	"N"	=>	"天冬酰胺(N)",
		"Ter"	=>	"终止密码子(Ter)",	"*"	=>	"终止密码子(*)"
	);
	if ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)([A-Z\*][a-z]{0,2})fs/) {	#frameshift
 		my $des = "第$2位氨基酸$aa_exp{$1}被氨基酸$aa_exp{$3}替代并发生移码";
		if ($phgvs =~ /fs\*(\d+)/) {
			$des.="，从此位置开始第$1位为终止密码子(*)";
		} elsif ($phgvs =~ /fs\*\?/) {
			$des.="，终止密码子位置无法确定";
		}
		return $des;
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)([a-z]{0,2})fs/) {	#frameshif
		my $des = "第$2位氨基酸$aa_exp{$1}被替代并发生移码";
		if ($phgvs =~ /fs\*(\d+)/) {
			$des .= "，从此位置开始第$1位为终止密码子(*)";
		}
		elsif ($phgvs =~ /fs\*\?/) {
			$des .= "，终止密码子位置无法确定";
		}
		return $des;
	} elsif ($phgvs=~/p\.([A-Z\*a-z]+)(\d+)dup$/){
		my $aa = $1;
		my $pos = $2;
		my $aa3_exp = "";
		while($aa=~s/^([A-Z\*][a-z]{0,2})//) {
                        $aa3_exp.=$aa_exp{$1};
                }
		return "第$pos位氨基酸$aa3_exp重复";
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)_([A-Z\*][a-z]{0,2})(\d+)dup$/) {	#dup more
		return "第$2位氨基酸$aa_exp{$1}到第$4位氨基酸$aa_exp{$3}重复";
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)=$/) { #同义突变
		return "第$2位氨基酸$aa_exp{$1}未发生改变";
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)([A-Z\*][a-z]{0,2})$/) {	# SNP
		return "第$2位氨基酸$aa_exp{$1}被氨基酸$aa_exp{$3}替代";
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)_([A-Z\*][a-z]{0,2})(\d+)ins([A-Z\*a-z]+)$/) {	#ins
 		my $aa1=$1;
		my $pos1=$2;
		my $aa2=$3;
		my $pos2=$4;
		my $aa3=$5;
		my $aa3_exp="";
		while($aa3=~s/^([A-Z\*][a-z]{0,2})//) {
			$aa3_exp.=$aa_exp{$1};
		}
		$aa3_exp=~s/终止/并生成终止/;
		return "第".$pos1."位氨基酸".$aa_exp{$aa1}."与第".$pos2."位氨基酸".$aa_exp{$aa2}."之间插入氨基酸".$aa3_exp;
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)del$/) {	#del one
		return "第$2位氨基酸$aa_exp{$1}缺失";
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)_([A-Z\*][a-z]{0,2})(\d+)del$/) {	#del more
		return "第$2位氨基酸$aa_exp{$1}到第$4位氨基酸$aa_exp{$3}缺失";
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)delins([A-Z\*a-z]+)$/) {	#delins, one to more
		my $aa1=$1;
		my $pos=$2;
		my $aa2=$3;
		my $aa2_exp="";
		while($aa2=~s/^([A-Z\*][a-z]{0,2})//) {
			$aa2_exp.=$aa_exp{$1};
		}
		$aa2_exp=~s/终止/并生成终止/;
 		return "第".$pos."位氨基酸".$aa_exp{$aa1}."缺失并插入氨基酸".$aa2_exp;
	} elsif ($phgvs=~/p\.([A-Z\*][a-z]{0,2})(\d+)_([A-Z\*][a-z]{0,2})(\d+)delins([A-Z\*a-z]+)$/) {	#delins, more to more
		my $aa1=$1;
		my $pos1=$2;
		my $aa2=$3;
		my $pos2=$4;
		my $aa3=$5;
		my $aa3_exp="";
		while($aa3=~s/^([A-Z\*][a-z]{0,2})//) {
			$aa3_exp.=$aa_exp{$1};
		}
		$aa3_exp=~s/终止/并生成终止/;
 		return "第".$pos1."位氨基酸".$aa_exp{$aa1}."到第".$pos2."位氨基酸".$aa_exp{$aa2}."缺失并插入氨基酸".$aa3_exp;
	}
}
1;

