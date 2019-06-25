import { Moment } from 'moment';
import { IVoteProposal } from 'app/shared/model/vote-proposal.model';

export const enum ProposalType {
  STUDY = 'STUDY',
  APPROVED = 'APPROVED',
  DEVELOPMENT = 'DEVELOPMENT',
  PRODUCTION = 'PRODUCTION'
}

export const enum ProposalRole {
  USER = 'USER',
  ORGANIZER = 'ORGANIZER',
  MANAGER = 'MANAGER',
  ADMIN = 'ADMIN'
}

export interface IProposal {
  id?: number;
  creationDate?: Moment;
  proposalName?: string;
  proposalType?: ProposalType;
  proposalRole?: ProposalRole;
  releaseDate?: Moment;
  proposalVotes?: number;
  proposalUserVotes?: number;
  voteProposals?: IVoteProposal[];
  proposalUserId?: number;
  postId?: number;
}

export class Proposal implements IProposal {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public proposalName?: string,
    public proposalType?: ProposalType,
    public proposalRole?: ProposalRole,
    public releaseDate?: Moment,
    public proposalVotes?: number,
    public proposalUserVotes?: number,
    public voteProposals?: IVoteProposal[],
    public proposalUserId?: number,
    public postId?: number
  ) {}
}
