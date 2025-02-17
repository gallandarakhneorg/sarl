#!/usr/bin/env perl

use strict;
use File::Basename;

my $CDIR = dirname("$0");
my $INIFILE = $ENV['DEVEL_SARL_INI_FILE'] || "$CDIR/sarldev.ini";

my $JVM_ROOT = "/usr/lib/jvm";
my $JVM_BIN_PATH = "bin";
my $JVM_JAVACBIN_PATH = "$JVM_BIN_PATH/javac";
my $JVM_JAVABIN_PATH = "$JVM_BIN_PATH/java";

sub findJdks(;$) {
	my $root = shift || '';
	if (!$root) {
		$root = $JVM_ROOT;
	}
	local *DIR;
	opendir(DIR, "$root") or die("$root: $!");
	my %jdks = ();
	while (my $file = readdir(DIR)) {
		if ($file =~ /^java\-([0-9.]+)\-([^-]+)/ ) {
			my $version = "$1";
			my $vendor = "$2";
			if ($version =~ /^1\.([0-9]+)/) {
				$version = "$1";
			} elsif ($version =~ /^([0-9]+)/) {
				$version = "$1";
			}
			my $dir = "$root/$file";
			my $bin = "$dir/$JVM_JAVACBIN_PATH";
			if (-x "$bin") {
				$jdks{"$version"}{"$vendor"} = "$dir";
			}
		}
	}
	closedir(DIR);
	return %jdks;
}

sub buildJdkKeys(\%) {
	my $jdkref = shift;
	my %jdks = %{$jdkref};
	my %keys = ();
	foreach my $version (keys %jdks) {
		my @k = keys %{$jdks{$version}};
		if (@k > 1) {
			foreach my $vendor (keys %{$jdks{$version}}) {
				$keys{"$version-$vendor"} = $jdks{$version}{$vendor};
			}
		} else {
			my $vendor = $k[0];
			my $jdkdir = $jdks{$version}{$vendor};
			$keys{"$version"} = $jdkdir;
		}
	}
	return %keys;
}

sub printUsage(@) {
	my @list = sort {
		my $cmp = $a <=> $b;
		if ($cmp == 0) {
			$cmp = $a cmp $b;
		}
		return $cmp;
	} @_;
	print STDERR ("usage: $0 [");
	my $first = 1;
	foreach my $k (@list) {
		if ($first) {
			$first = 0;
		} else {
			print STDERR (" | ");
		}
		print STDERR ("$k");
	}
	print STDERR ("]\n");
	exit(255);
}

sub printUsage2($@) {
	my $content = shift || '';
	if ($content =~ /-vm[\n\r]+([^\n\r]+)[\n\r]+/s) {
		my $jvm = "$1";
		print STDERR ("Current JVM: $jvm\n");
	} elsif ($content =~ /-vm[\n\r]+([^\n\r]+)$/s) {
		my $jvm = "$1";
		print STDERR ("Current JVM: $jvm\n");
	}
	printUsage(@_);
}

sub checkIniFile(@) {
	unless (-r "$INIFILE") {
		print STDERR ("$INIFILE: Cannot read\n");
		printUsage(@_);
	}
}

sub readIniFile() {
	local *FID;
	open(*FID, "< $INIFILE") or die("$INIFILE: $!\n");
	my $content = '';
	while (my $line = <FID>) {
		$content .= "$line";
	}
	close(*FID);
	return "$content";
}

sub selectJvm(\%) {
	my $jdkref = shift;
	my %jdks = %{$jdkref};
	my $jvm = '';
	if ($ARGV[0]) {
		foreach my $k (keys %jdks) {
			if ($ARGV[0] eq $k) {
				return $jdks{$k};
			}
		}
	}
	return $jvm;
}

sub updateIniContent(@) {
	my $jvm = shift || die("no JVM provided\n");
	# Check the JVM path
	my $jvmfolder = "$jvm";
	if (-x "$jvm/$JVM_JAVABIN_PATH") {
		$jvmfolder = "$jvm/$JVM_BIN_PATH";
	}
	my $content = shift || die("no content\n");
	if ($content =~ /^-vm$/m) {
		$content =~ s/-vm[\n\r]+[^\n\r]+[\n\r]+/-vm\n$jvmfolder\n/s;
	} else {
		$content =~ s/^-vmargs/-vm\n$jvmfolder\n-vmargs/m;
	}
	return $content;
}

sub writeIniFile($) {
	my $content = $_[0] or die("not enough argument\n");
	unless (-w "$INIFILE") {
		print STDERR ("$INIFILE: Cannot write\n");
		usage();
	}
	open(*FID, "> $INIFILE") or die("$INIFILE: $!\n");
	print FID ($content);
	close(*FID);
}

my %jdks =  findJdks();
my %jdkkeys = buildJdkKeys(%jdks);
checkIniFile(keys %jdkkeys);
my $content = readIniFile();

my $jvm = selectJvm(%jdkkeys);

if (!$jvm) {
	printUsage2("$content", keys %jdkkeys);
}

print STDOUT ("Forcing JVM to: $jvm\n");


print STDOUT ("Ini file: $INIFILE\n"); 

$content = updateIniContent("$jvm", $content);

writeIniFile("$content"); 

exit(0);

