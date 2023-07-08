import webpack, { DefinePlugin, RuleSetRule } from 'webpack';
import path from 'path';
import { buildCssLoader } from '../build/loaders/buildCssLoader';
import { BuildPaths } from '../build/types/config';

export default ({ config }: { config: webpack.Configuration }) => {
    const paths: BuildPaths = {
        build: '',
        html: '',
        entry: '',
        src: path.resolve(__dirname, '..', '..', 'src'),
    };
    if (config!.resolve!.modules) {
    config!.resolve!.modules = [
        path.resolve(__dirname, '../../src'),
        'node_modules',
    ];
    }
  config!.resolve!.extensions!.push('.ts', '.tsx');

  // eslint-disable-next-line no-param-reassign
  // @ts-ignore
  config!.module!.rules = config!.module!.rules!.map((rule: RuleSetRule) => {
      if (/svg/.test(rule.test as string)) {
          return { ...rule, exclude: /\.svg$/i };
      }

      return rule;
  });

  config!.module!.rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
  });
  config!.module!.rules.push(buildCssLoader(true));

  config!.plugins!.push(
      new DefinePlugin({
          __IS_DEV__: true,
          __API__: '""',
      }),
  );

  return config;
};