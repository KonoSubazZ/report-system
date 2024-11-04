#!usr/bin/perl
use warnings;
use strict;
use Getopt::Long;
use FindBin qw($Bin);
use lib "$Bin";
use mutation_explanation;
die "Usage: $0 Mutation MutFreq" if (@ARGV < 2);
my ($gene, $mutation, $freq) = @ARGV;

my $site_description = "";
if ($mutation =~ /Amplification/i) {
	$site_description = "$gene发生基因扩增";
	if ($freq =~/X/) {
		$site_description .= "，此突变在样本中的扩增倍数为$freq" if ($freq and $freq ne '.');
	} else {
		$site_description .= "，此突变在样本中的拷贝数为$freq" if ($freq and $freq ne '.');
	}
	$site_description.= "。";
} elsif($mutation =~/Fusion/i) {
	my $geneStr = (split / /, $mutation)[0];
	my $bpStr = (split / /, $mutation)[2];
	my ($gene1,$gene2,$bp1,$bp2);
	if (index($geneStr,$gene) == 0) {
		$gene1 = $gene;
		$gene2 = substr($geneStr, length($gene)+ 1);
	} else {
		$gene2 = $gene;
		$gene1 = substr($geneStr, 0, length($geneStr) - length($gene) - 1);
	}
	my $str1 = substr((split /:/, $bpStr)[0], 1);
	if ($str1 =~ /(\d+)/) {
		$bp1 = $1;
	}
	my $str2 = substr((split /:/, $bpStr)[1], 1);
	if ($str2 =~ /(\d+)/) {
		$bp2 = $1;
	}
    if ($str1 =~/intergenic/ && $str2 =~/intergenic/) {
        $site_description = "基因间区和基因间区发生融合。";
    } elsif ($str1 =~/intergenic/) {
		$site_description = "基因间区和基因$gene2的$bp2号外显子发生融合。";
	} elsif($str2 =~/intergenic/) {
		$site_description = "基因$gene1的$bp1号外显子和基因间区发生融合。";
	} else {
		$site_description = "基因$gene1的$bp1号外显子和基因$gene2的$bp2号外显子发生融合。";
	}
} else {
	my $hgvs = mutation_explanation->new();
	my @info = split / /, $mutation;
	shift @info;
	#if ($mutation=~/exon/ && $mutation !~/intron/){
	if ($info[2] =~ /p\./){
		$site_description = $hgvs->chgvs($info[0],$info[1])."，导致相应蛋白序列中";
		$site_description .= $hgvs->phgvs($info[2]);
	} elsif ($mutation =~/intron/ || $mutation =~/promoter/) {
		$site_description = $hgvs->chgvs($info[0],$info[1]);
	}
	if ($freq =~ /合/) {
		$site_description .= "，此突变在样本中的基因型为$freq" if ($freq and $freq ne '.');
	} else {
		$site_description .= "，此突变在样本中的变异丰度为$freq\%" if ($freq and $freq ne '.');
	}
	$site_description.= "。";
}
if($gene eq "Complex" || $gene eq "MSI" || $gene eq "TMB" || $gene eq "bTMB"){$site_description="";}
print "$site_description\n";
