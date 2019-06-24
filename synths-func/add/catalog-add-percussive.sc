(

// the functions here
// adds percussive elements to a SynthDef, such as noisy initial strike or bass-drum like tones

ClioLibrary.catalog ([\func, \add, \percussive], {

	// rand tones from sine waves
	// at low freqs with perc env this works as a bass drum, at mid/high freqs it's a funny alien sounding thingy
	~drumy = { arg kwargs;

		var rand_tones, tone_sig, tone_env, click_sig, click_env;

		var numTones = kwargs[\def_drumy_numTones] ? 9;
		var tonesSpread = kwargs[\def_drumy_tonesSpread] ? 0.4;

		var freqSpread = \drumyFreqSpread.ir( kwargs[\drumyFreqSpread] ? 1.5);
		var randFreqs = { ExpRand(kwargs[\freq]/freqSpread, kwargs[\freq]*freqSpread) }!numTones;
		var randAmps = { Rand(0.6/numTones, 1.2/numTones) }! numTones;

		var tones = Klang.ar(`[randFreqs, randAmps]);

		var sig = Splay.ar(tones, tonesSpread) * AmpComp.kr(kwargs[\freq], 90);

		kwargs[\sig] = kwargs[\sig] + sig;

	};

	// WARNING: THESE noiseHit synths DO NOT FREE THE SYNTH
	// (most common use would be to combine with other functions that do free the synth)
	// ... so if they're used independently, they should be combined with DetectSilence

	~noiseHit1 = { arg kwargs;
		var genType = kwargs[\def_noiseHit1_genType] ? PinkNoise;
		var lpfFreq = kwargs[\def_noiseHit1_lpfFreq] ? 166;
		var ampScale = kwargs[\def_noiseHit1_ampScale] ? 0.9;
		var attackTime = kwargs[\def_noiseHit1_attackTime] ? 0.01;
		var releaseTime = kwargs[\def_noiseHit1_releaseTime] ? 1;
		var curve = kwargs[\def_noiseHit1_curve] ? -4;

		var sig = LPF.ar(genType.ar(0.9!2), lpfFreq);
		var env = EnvGen.kr(Env.perc(attackTime:attackTime, releaseTime:releaseTime, level:kwargs[\amp], curve:curve));

		kwargs[\sig] = kwargs[\sig] + sig * env;

	};

	~noiseHit2 = { arg kwargs;
		var genType = kwargs[\def_noiseHit2_genType] ? WhiteNoise;
		var lpfFreq = kwargs[\def_noiseHit2_lpfFreq] ? 166;
		var ampScale = kwargs[\def_noiseHit2_ampScale] ? 0.9;
		var attackTime = kwargs[\def_noiseHit2_attackTime] ? 0.01;
		var releaseTime = kwargs[\def_noiseHit2_releaseTime] ? 1;
		var curve = kwargs[\def_noiseHit2_curve] ? -16;

		var sig = Resonz.ar(genType.ar(ampScale!2), kwargs[\freq]*2, 0.4, 2) * AmpComp.kr(kwargs[\freq], 60, 0.69);
		var env = EnvGen.kr(Env.perc(attackTime:attackTime, releaseTime:releaseTime, level:kwargs[\amp], curve:curve));

		kwargs[\sig] = kwargs[\sig] + sig * env;

	}


		// sig2 =
		// env2 = EnvGen.kr(Env.perc(attackTime:0.01, releaseTime:0.2, curve:-16), gate:kwargs[\gate];






}
);

)
