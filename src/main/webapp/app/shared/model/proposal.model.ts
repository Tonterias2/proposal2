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
  isOpen?: boolean;
  isAccepted?: boolean;
  isPaid?: boolean;
  voteProposals?: IVoteProposal[];
  profileId?: number;
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
    public isOpen?: boolean,
    public isAccepted?: boolean,
    public isPaid?: boolean,
    public voteProposals?: IVoteProposal[],
    public profileId?: number,
    public postId?: number
  ) {
    this.isOpen = this.isOpen || false;
    this.isAccepted = this.isAccepted || false;
    this.isPaid = this.isPaid || false;
  }
}
