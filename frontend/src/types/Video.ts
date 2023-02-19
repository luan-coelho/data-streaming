import ResolutionPath from './ResolutionPath';

export default interface Video {
  id: number
  title: string
  description: string
  resolutionPaths: ResolutionPath[]
}